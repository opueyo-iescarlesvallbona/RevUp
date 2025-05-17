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
    public class MemberLocationsController : Controller
    {
        private readonly RevupContext _context;

        public MemberLocationsController(RevupContext context)
        {
            _context = context;
        }

        // GET: MemberLocations
        public async Task<IActionResult> Index()
        {
            return View(await _context.MemberLocations.ToListAsync());
        }

        // GET: MemberLocations/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var memberLocation = await _context.MemberLocations
                .FirstOrDefaultAsync(m => m.Id == id);
            if (memberLocation == null)
            {
                return NotFound();
            }

            return View(memberLocation);
        }

        // GET: MemberLocations/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: MemberLocations/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Municipality,Ccaa,Country,Latitude,Longitude")] MemberLocation memberLocation)
        {
            if (ModelState.IsValid)
            {
                _context.Add(memberLocation);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(memberLocation);
        }

        // GET: MemberLocations/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var memberLocation = await _context.MemberLocations.FindAsync(id);
            if (memberLocation == null)
            {
                return NotFound();
            }
            return View(memberLocation);
        }

        // POST: MemberLocations/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Municipality,Ccaa,Country,Latitude,Longitude")] MemberLocation memberLocation)
        {
            if (id != memberLocation.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(memberLocation);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!MemberLocationExists(memberLocation.Id))
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
            return View(memberLocation);
        }

        // GET: MemberLocations/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var memberLocation = await _context.MemberLocations
                .FirstOrDefaultAsync(m => m.Id == id);
            if (memberLocation == null)
            {
                return NotFound();
            }

            return View(memberLocation);
        }

        // POST: MemberLocations/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var memberLocation = await _context.MemberLocations.FindAsync(id);
            if (memberLocation != null)
            {
                _context.MemberLocations.Remove(memberLocation);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool MemberLocationExists(int id)
        {
            return _context.MemberLocations.Any(e => e.Id == id);
        }

        [Authorize]
        [Route("api/LocationById")]
        [HttpGet]
        public async Task<ActionResult<MemberLocation>> GetLocationById([FromQuery] int id)
        {
            var location = await _context.MemberLocations.FindAsync(id);
            if (location == null)
            {
                return NotFound();
            }
            return Ok(location);
        }

        [Authorize]
        [Route("api/LocationsByCountry")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<MemberLocation>>> GetLocationsByCountry([FromQuery] string country)
        {
            var locations = await _context.MemberLocations
                .Where(l => l.Country.Equals(country, StringComparison.OrdinalIgnoreCase))
                .ToListAsync();
            if (locations == null || !locations.Any())
            {
                return NotFound();
            }
            return Ok(locations);
        }

        [Authorize]
        [Route("api/LocationByMunicipality")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<MemberLocation>>> GetLocationsByMunicipality([FromQuery] string municipality)
        {
            var locations = await _context.MemberLocations.Where(l => l.Municipality.Equals(municipality)).ToListAsync();
            if (locations == null || !locations.Any())
            {
                return NotFound();
            }
            return Ok(locations);
        }

        [Authorize]
        [Route("api/LocationsByCcaa")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<MemberLocation>>> GetLocationsByCcaa([FromQuery] string ccaa)
        {
            var locations = await _context.MemberLocations
                .Where(l => l.Ccaa.Equals(ccaa, StringComparison.OrdinalIgnoreCase))
                .ToListAsync();
            if (locations == null || !locations.Any())
            {
                return NotFound();
            }
            return Ok(locations);
        }

        [Authorize]
        [Route("api/Locations")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<MemberLocation>>> GetAllLocations()
        {
            var locations = await _context.MemberLocations.ToListAsync();
            if (locations == null || !locations.Any())
            {
                return NotFound();
            }
            return Ok(locations);
        }

        [Authorize]
        [Route("api/Location")]
        [HttpPost]
        public async Task<ActionResult<MemberLocation>> PôstLocation([FromBody] MemberLocation memberLocation)
        {
            if (memberLocation == null)
            {
                return BadRequest("MemberLocation cannot be null.");
            }
            _context.MemberLocations.Add(memberLocation);
            await _context.SaveChangesAsync();
            return memberLocation;
        }

    }
}
