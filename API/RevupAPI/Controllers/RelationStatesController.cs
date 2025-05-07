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
    public class RelationStatesController : Controller
    {
        private readonly RevupContext _context;

        public RelationStatesController(RevupContext context)
        {
            _context = context;
        }

        // GET: RelationStates
        public async Task<IActionResult> Index()
        {
            return View(await _context.RelationStates.ToListAsync());
        }

        // GET: RelationStates/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var relationState = await _context.RelationStates
                .FirstOrDefaultAsync(m => m.Id == id);
            if (relationState == null)
            {
                return NotFound();
            }

            return View(relationState);
        }

        // GET: RelationStates/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: RelationStates/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name")] RelationState relationState)
        {
            if (ModelState.IsValid)
            {
                _context.Add(relationState);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(relationState);
        }

        // GET: RelationStates/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var relationState = await _context.RelationStates.FindAsync(id);
            if (relationState == null)
            {
                return NotFound();
            }
            return View(relationState);
        }

        // POST: RelationStates/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name")] RelationState relationState)
        {
            if (id != relationState.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(relationState);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!RelationStateExists(relationState.Id))
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
            return View(relationState);
        }

        // GET: RelationStates/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var relationState = await _context.RelationStates
                .FirstOrDefaultAsync(m => m.Id == id);
            if (relationState == null)
            {
                return NotFound();
            }

            return View(relationState);
        }

        // POST: RelationStates/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var relationState = await _context.RelationStates.FindAsync(id);
            if (relationState != null)
            {
                _context.RelationStates.Remove(relationState);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool RelationStateExists(int id)
        {
            return _context.RelationStates.Any(e => e.Id == id);
        }

        [Authorize]
        [Route("api/RelationStateById")]
        [HttpGet]
        public async Task<ActionResult<RelationState>> GetRelationStateById([FromQuery] int id)
        {
            var relationState = await _context.RelationStates.Where(x=>x.Id==id).FirstOrDefaultAsync();
            if (relationState == null)
            {
                return NotFound();
            }
            return relationState;
        }

        [Authorize]
        [Route("api/RelationStateByName")]
        [HttpGet]
        public async Task<ActionResult<RelationState>> GetRelationStateByName([FromQuery] string name)
        {
            var relationState = await _context.RelationStates.Where(x => x.Name == name).FirstOrDefaultAsync();
            if (relationState == null)
            {
                return NotFound();
            }
            return relationState;
        }
    }
}
