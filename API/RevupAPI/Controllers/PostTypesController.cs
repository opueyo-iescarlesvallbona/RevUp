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
    public class PostTypesController : Controller
    {
        private readonly RevupContext _context;

        public PostTypesController(RevupContext context)
        {
            _context = context;
        }

        // GET: PostTypes
        public async Task<IActionResult> Index()
        {
            return View(await _context.PostTypes.ToListAsync());
        }

        // GET: PostTypes/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var postType = await _context.PostTypes
                .FirstOrDefaultAsync(m => m.Id == id);
            if (postType == null)
            {
                return NotFound();
            }

            return View(postType);
        }

        // GET: PostTypes/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: PostTypes/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name")] PostType postType)
        {
            if (ModelState.IsValid)
            {
                _context.Add(postType);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(postType);
        }

        // GET: PostTypes/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var postType = await _context.PostTypes.FindAsync(id);
            if (postType == null)
            {
                return NotFound();
            }
            return View(postType);
        }

        // POST: PostTypes/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name")] PostType postType)
        {
            if (id != postType.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(postType);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!PostTypeExists(postType.Id))
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
            return View(postType);
        }

        // GET: PostTypes/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var postType = await _context.PostTypes
                .FirstOrDefaultAsync(m => m.Id == id);
            if (postType == null)
            {
                return NotFound();
            }

            return View(postType);
        }

        // POST: PostTypes/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var postType = await _context.PostTypes.FindAsync(id);
            if (postType != null)
            {
                _context.PostTypes.Remove(postType);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool PostTypeExists(int id)
        {
            return _context.PostTypes.Any(e => e.Id == id);
        }

        [Authorize]
        [Route("api/PostTypeById")]
        [HttpGet]
        public async Task<ActionResult<PostType>> GetPostTypeById([FromQuery] int id)
        {
            var postType = await _context.PostTypes.FindAsync(id);
            if (postType == null)
            {
                return NotFound();
            }
            return postType;
        }

        [Authorize]
        [Route("api/PostTypeByName")]
        [HttpGet]
        public async Task<ActionResult<PostType>> GetPostTypesByName([FromQuery] string name)
        {
            var postType = await _context.PostTypes.Where(x=>x.Name.Equals(name)).FirstOrDefaultAsync();
            if (postType == null)
            {
                return NotFound();
            }
            return postType;
        }
    }
}
