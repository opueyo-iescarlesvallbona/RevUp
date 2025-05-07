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
    public class TerrainTypesController : Controller
    {
        private readonly RevupContext _context;

        public TerrainTypesController(RevupContext context)
        {
            _context = context;
        }

        // GET: TerrainTypes
        public async Task<IActionResult> Index()
        {
            return View(await _context.TerrainTypes.ToListAsync());
        }

        // GET: TerrainTypes/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var terrainType = await _context.TerrainTypes
                .FirstOrDefaultAsync(m => m.Id == id);
            if (terrainType == null)
            {
                return NotFound();
            }

            return View(terrainType);
        }

        // GET: TerrainTypes/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: TerrainTypes/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name")] TerrainType terrainType)
        {
            if (ModelState.IsValid)
            {
                _context.Add(terrainType);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(terrainType);
        }

        // GET: TerrainTypes/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var terrainType = await _context.TerrainTypes.FindAsync(id);
            if (terrainType == null)
            {
                return NotFound();
            }
            return View(terrainType);
        }

        // POST: TerrainTypes/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name")] TerrainType terrainType)
        {
            if (id != terrainType.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(terrainType);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!TerrainTypeExists(terrainType.Id))
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
            return View(terrainType);
        }

        // GET: TerrainTypes/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var terrainType = await _context.TerrainTypes
                .FirstOrDefaultAsync(m => m.Id == id);
            if (terrainType == null)
            {
                return NotFound();
            }

            return View(terrainType);
        }

        // POST: TerrainTypes/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var terrainType = await _context.TerrainTypes.FindAsync(id);
            if (terrainType != null)
            {
                _context.TerrainTypes.Remove(terrainType);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool TerrainTypeExists(int id)
        {
            return _context.TerrainTypes.Any(e => e.Id == id);
        }

        [Authorize]
        [Route("api/TerrainTypeById")]
        [HttpGet]
        public async Task<ActionResult<TerrainType>> GetTerrainTypeById([FromQuery] int id)
        {
            var terrainType = await _context.TerrainTypes.FindAsync(id);
            if (terrainType == null)
            {
                return NotFound();
            }
            return terrainType;
        }

        [Authorize]
        [Route("api/TerrainTypeByName")]
        [HttpGet]
        public async Task<ActionResult<TerrainType>> GetTerrainTypeByName([FromQuery] string name)
        {
            var terrainType = await _context.TerrainTypes.Where(x=>x.Name.Equals(name)).FirstOrDefaultAsync();
            if (terrainType == null)
            {
                return NotFound();
            }
            return terrainType;
        }
    }
}
