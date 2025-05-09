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
    public class PostsController : Controller
    {
        private readonly RevupContext _context;

        public PostsController(RevupContext context)
        {
            _context = context;
        }

        // GET: Posts
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.Posts.Include(p => p.Member).Include(p => p.PostTypeNavigation).Include(p => p.Route);
            return View(await revupContext.ToListAsync());
        }

        // GET: Posts/Details/5
        [Route("api/details/{id}")]
        [HttpGet]
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var post = await _context.Posts
                .Include(p => p.Member)
                .Include(p => p.PostTypeNavigation)
                .Include(p => p.Route)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (post == null)
            {
                return NotFound();
            }

            return View(post);
        }

        // GET: Posts/Create
        public IActionResult Create()
        {
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["PostType"] = new SelectList(_context.PostTypes, "Id", "Id");
            ViewData["RouteId"] = new SelectList(_context.Routes, "Id", "Id");
            return View();
        }

        // POST: Posts/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Title,PostType,Description,PostDate,Picture,Likes,Address,RouteId,MemberId,Comments")] Post post)
        {
            if (ModelState.IsValid)
            {
                _context.Add(post);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", post.MemberId);
            ViewData["PostType"] = new SelectList(_context.PostTypes, "Id", "Id", post.PostType);
            ViewData["RouteId"] = new SelectList(_context.Routes, "Id", "Id", post.RouteId);
            return View(post);
        }

        // GET: Posts/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var post = await _context.Posts.FindAsync(id);
            if (post == null)
            {
                return NotFound();
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", post.MemberId);
            ViewData["PostType"] = new SelectList(_context.PostTypes, "Id", "Id", post.PostType);
            ViewData["RouteId"] = new SelectList(_context.Routes, "Id", "Id", post.RouteId);
            return View(post);
        }

        // POST: Posts/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Title,PostType,Description,PostDate,Picture,Likes,Address,RouteId,MemberId,Comments")] Post post)
        {
            if (id != post.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(post);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!PostExists(post.Id))
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
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", post.MemberId);
            ViewData["PostType"] = new SelectList(_context.PostTypes, "Id", "Id", post.PostType);
            ViewData["RouteId"] = new SelectList(_context.Routes, "Id", "Id", post.RouteId);
            return View(post);
        }

        // GET: Posts/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var post = await _context.Posts
                .Include(p => p.Member)
                .Include(p => p.PostTypeNavigation)
                .Include(p => p.Route)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (post == null)
            {
                return NotFound();
            }

            return View(post);
        }

        // POST: Posts/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var post = await _context.Posts.FindAsync(id);
            if (post != null)
            {
                _context.Posts.Remove(post);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool PostExists(int id)
        {
            return _context.Posts.Any(e => e.Id == id);
        }

        [Route("api/PostsByLocationId")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Post>>> GetPostsByLocation([FromQuery] int location_id)
        {
            var posts = await _context.Posts.Where(x => x.LocationId==location_id).ToListAsync();

            if (posts == null || !posts.Any())
            {
                return NotFound();
            }
            return posts;
        }

        [Authorize]
        [Route("api/PostsByLikes")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Post>>> GetPostsByLikes()
        {
            var posts = await _context.Posts.OrderByDescending(x => x.Likes).ToListAsync();

            return posts;
        }

        [Authorize]
        [Route("api/PostsByMemberFriends")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Post>>> GetPostsByMemberFriends([FromQuery] int memberId)
        {
            var member = await _context.MemberRelations.Where(x=>x.MemberId1==memberId&&x.State.Name.Equals("Friend")).Select(x=>x.MemberId2).ToListAsync();

            if (member == null)
            {
                return NotFound();
            }
            var posts = await _context.Posts
                .Where(p => member.Contains(p.MemberId))
                .ToListAsync();
            if (posts == null || !posts.Any())
            {
                return NotFound();
            }
            
            return posts;
        }

        [Authorize]
        [Route("api/PostById")]
        [HttpGet]
        public async Task<ActionResult<Post>> GetPost([FromQuery] int id)
        {
            var post = await _context.Posts.FirstOrDefaultAsync(m => m.Id == id);
            if (post == null)
            {
                return NotFound();
            }
            return post;
        }

        [Route("api/Post")]
        [HttpPost]

        public async Task<IActionResult> PostPost([FromQuery] IFormFile image, [FromBody] Post post)
        {
            if (ModelState.IsValid)
            {
                _context.Posts.Add(post);
                await _context.SaveChangesAsync();

                if (image != null)
                {
                    try
                    {
                        GeneralController.UploadImage(image, post);
                    }
                    catch { }
                }
                return Ok(post);
            }
            return BadRequest("Invalid post data");
        }

        [Route("api/Post")]
        [HttpPut]
        public async Task<IActionResult> PutPost([FromBody] Post post)
        {
            _context.Entry(post).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PostExists(post.Id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }
            return NoContent();
        }

        [Route("api/Post")]
        [HttpDelete]
        public async Task<bool> DeletePost([FromQuery] int id)
        {
            var post = await _context.Posts.FindAsync(id);

            if (post == null)
            {
                return false;
            }

            var comments = await _context.PostComments.Where(c => c.PostId == id).ToListAsync();

            if (comments != null)
            {
                _context.PostComments.RemoveRange(comments);
            }

            _context.Posts.Remove(post);
            await _context.SaveChangesAsync();
            return true;
        }

        [Route("api/PostLike")]
        [HttpPost]
        public async Task<IActionResult> LikePost([FromQuery]int memberId, [FromQuery]int postId)
        {
            var post = await _context.Posts.FindAsync(postId);
            var member = await _context.Members.FindAsync(memberId);
            if (post == null || member == null)
            {
                return NotFound();
            }
            post.Members.Add(member);
            await _context.SaveChangesAsync();
            return Ok(post);
        }

        [Route("api/PostUnLike")]
        [HttpDelete]
        public async Task<IActionResult> UnlikePost([FromQuery] int memberId, [FromQuery] int postId)
        {
            var post = await _context.Posts.FindAsync(postId);
            var member = await _context.Members.FindAsync(memberId);
            if (post == null || member == null)
            {
                return NotFound();
            }
            post.Members.Remove(member);
            await _context.SaveChangesAsync();
            return Ok(post);
        }

        [Authorize]
        [Route("api/PostIsLikedByMember")]
        [HttpGet]
        public async Task<ActionResult<bool>> IsPostLiked([FromQuery] int memberId, [FromQuery] int postId)
        {
            var post = await _context.Posts.FindAsync(postId);
            var member = await _context.Members.FindAsync(memberId);
            if (post == null || member == null)
            {
                return NotFound();
            }
            var isLiked = post.Members.Contains(member);
            return isLiked;
        }
    }
}
