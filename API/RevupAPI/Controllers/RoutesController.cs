using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.ConstrainedExecution;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using RevupAPI.Models;

namespace RevupAPI.Controllers
{
    public class RoutesController : Controller
    {
        private readonly RevupContext _context;

        public RoutesController(RevupContext context)
        {
            _context = context;
        }

        // GET: Routes
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.Routes.Include(r => r.Member).Include(r => r.TerrainType);
            return View(await revupContext.ToListAsync());
        }

        // GET: Routes/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var route = await _context.Routes
                .Include(r => r.Member)
                .Include(r => r.TerrainType)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (route == null)
            {
                return NotFound();
            }

            return View(route);
        }

        // GET: Routes/Create
        public IActionResult Create()
        {
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["TerrainTypeId"] = new SelectList(_context.TerrainTypes, "Id", "Id");
            return View();
        }

        // POST: Routes/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name,Waypoints,Distance,Duration,MaxElevation,ElevationGain,StartAddress,EndAddress,TerrainTypeId,Description,MemberId,Datetime")] Models.Route route)
        {
            if (ModelState.IsValid)
            {
                _context.Add(route);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", route.MemberId);
            ViewData["TerrainTypeId"] = new SelectList(_context.TerrainTypes, "Id", "Id", route.TerrainTypeId);
            return View(route);
        }

        // GET: Routes/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var route = await _context.Routes.FindAsync(id);
            if (route == null)
            {
                return NotFound();
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", route.MemberId);
            ViewData["TerrainTypeId"] = new SelectList(_context.TerrainTypes, "Id", "Id", route.TerrainTypeId);
            return View(route);
        }

        // POST: Routes/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name,Waypoints,Distance,Duration,MaxElevation,ElevationGain,StartAddress,EndAddress,TerrainTypeId,Description,MemberId,Datetime")] Models.Route route)
        {
            if (id != route.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(route);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!RouteExists(route.Id))
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
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", route.MemberId);
            ViewData["TerrainTypeId"] = new SelectList(_context.TerrainTypes, "Id", "Id", route.TerrainTypeId);
            return View(route);
        }

        // GET: Routes/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var route = await _context.Routes
                .Include(r => r.Member)
                .Include(r => r.TerrainType)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (route == null)
            {
                return NotFound();
            }

            return View(route);
        }

        // POST: Routes/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var route = await _context.Routes.FindAsync(id);
            if (route != null)
            {
                _context.Routes.Remove(route);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool RouteExists(int id)
        {
            return _context.Routes.Any(e => e.Id == id);
        }

        [Route("api/Route")]
        [HttpPost]
        public async Task<IActionResult> Route([FromBody] Models.Route route)
        {
            if (ModelState.IsValid)
            {
                _context.Routes.Add(route);
                await _context.SaveChangesAsync();
                return Ok(route);
            }
            return BadRequest("Invalid route data");
        }

        [Route("api/Routes/{id}")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Models.Route>>> GetRoutesByMember(int id)
        {
            var routes = await _context.Routes.Where(x=>x.MemberId==id).ToListAsync();
            if (routes == null || !routes.Any())
            {
                return NotFound();
            }
            return routes;
        }

        [Route("api/Route/{id}")]
        [HttpGet]
        public async Task<ActionResult<Models.Route>> GetRoute(int id)
        {
            var route = await _context.Routes.FindAsync(id);
            if (route == null)
            {
                return NotFound();
            }
            return route;
        }
    }
}
