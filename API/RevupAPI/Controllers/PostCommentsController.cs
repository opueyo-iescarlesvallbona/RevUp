using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using RevupAPI.Models;

namespace RevupAPI.Controllers
{
    public class PostCommentsController : Controller
    {
        private readonly RevupContext _context;

        public PostCommentsController(RevupContext context)
        {
            _context = context;
        }

        // GET: PostComments
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.PostComments.Include(p => p.Member).Include(p => p.Post);
            return View(await revupContext.ToListAsync());
        }

        // GET: PostComments/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var postComment = await _context.PostComments
                .Include(p => p.Member)
                .Include(p => p.Post)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (postComment == null)
            {
                return NotFound();
            }

            return View(postComment);
        }

        // GET: PostComments/Create
        public IActionResult Create()
        {
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["PostId"] = new SelectList(_context.Posts, "Id", "Id");
            return View();
        }

        // POST: PostComments/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,PostId,MemberId,CommentContent,Datetime")] PostComment postComment)
        {
            if (ModelState.IsValid)
            {
                _context.Add(postComment);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", postComment.MemberId);
            ViewData["PostId"] = new SelectList(_context.Posts, "Id", "Id", postComment.PostId);
            return View(postComment);
        }

        // GET: PostComments/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var postComment = await _context.PostComments.FindAsync(id);
            if (postComment == null)
            {
                return NotFound();
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", postComment.MemberId);
            ViewData["PostId"] = new SelectList(_context.Posts, "Id", "Id", postComment.PostId);
            return View(postComment);
        }

        // POST: PostComments/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,PostId,MemberId,CommentContent,Datetime")] PostComment postComment)
        {
            if (id != postComment.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(postComment);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!PostCommentExists(postComment.Id))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", postComment.MemberId);
            ViewData["PostId"] = new SelectList(_context.Posts, "Id", "Id", postComment.PostId);
            return View(postComment);
        }

        // GET: PostComments/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var postComment = await _context.PostComments
                .Include(p => p.Member)
                .Include(p => p.Post)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (postComment == null)
            {
                return NotFound();
            }

            return View(postComment);
        }

        // POST: PostComments/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var postComment = await _context.PostComments.FindAsync(id);
            if (postComment != null)
            {
                _context.PostComments.Remove(postComment);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool PostCommentExists(int id)
        {
            return _context.PostComments.Any(e => e.Id == id);
        }

        [Authorize]
        [Route("api/Comment")]
        [HttpPost]
        public async Task<ActionResult<bool>> PostComment([FromBody] PostComment postComment)
        {
            try
            {
                _context.PostComments.Add(postComment);
                await _context.SaveChangesAsync();
                return Ok(true);
            }
            catch
            {
                return BadRequest(false);
            }
            
        }

        [Authorize]
        [Route("api/Comment")]
        [HttpDelete]
        public async Task<bool> DeleteComment([FromQuery]int id)
        {
            var postComment = await _context.PostComments.FindAsync(id);
            if (postComment == null)
            {
                return false;
            }
            _context.PostComments.Remove(postComment);
            await _context.SaveChangesAsync();
            return true;
        }

        [Authorize]
        [Route("api/CommentsByPostId")]
        [HttpGet]
        public async Task<ActionResult<List<PostComment>>> GetCommentsByPostId([FromQuery] int postId)
        {
            var comments = await _context.PostComments.Where(x=>x.PostId==postId).ToListAsync();
            if (comments == null || !comments.Any())
            {
                return NotFound();
            }
            return Ok(comments);
        }

    }
}
