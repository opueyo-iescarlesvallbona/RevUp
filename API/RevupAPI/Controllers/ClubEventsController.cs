using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.ConstrainedExecution;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using RevupAPI.Models;

namespace RevupAPI.Controllers
{
    public class ClubEventsController : Controller
    {
        private readonly RevupContext _context;

        public ClubEventsController(RevupContext context)
        {
            _context = context;
        }

        // GET: ClubEvents
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.ClubEvents.Include(c => c.Club).Include(c => c.StateNavigation);
            return View(await revupContext.ToListAsync());
        }

        // GET: ClubEvents/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var clubEvent = await _context.ClubEvents
                .Include(c => c.Club)
                .Include(c => c.StateNavigation)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (clubEvent == null)
            {
                return NotFound();
            }

            return View(clubEvent);
        }

        // GET: ClubEvents/Create
        public IActionResult Create()
        {
            ViewData["ClubId"] = new SelectList(_context.Clubs, "Id", "Id");
            ViewData["State"] = new SelectList(_context.EventStates, "Id", "Id");
            return View();
        }

        // POST: ClubEvents/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name,Address,ClubId,Picture,StartDate,RouteStartDate,EndDate,Description,State")] ClubEvent clubEvent)
        {
            if (ModelState.IsValid)
            {
                _context.Add(clubEvent);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["ClubId"] = new SelectList(_context.Clubs, "Id", "Id", clubEvent.ClubId);
            ViewData["State"] = new SelectList(_context.EventStates, "Id", "Id", clubEvent.State);
            return View(clubEvent);
        }

        // GET: ClubEvents/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var clubEvent = await _context.ClubEvents.FindAsync(id);
            if (clubEvent == null)
            {
                return NotFound();
            }
            ViewData["ClubId"] = new SelectList(_context.Clubs, "Id", "Id", clubEvent.ClubId);
            ViewData["State"] = new SelectList(_context.EventStates, "Id", "Id", clubEvent.State);
            return View(clubEvent);
        }

        // POST: ClubEvents/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name,Address,ClubId,Picture,StartDate,RouteStartDate,EndDate,Description,State")] ClubEvent clubEvent)
        {
            if (id != clubEvent.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(clubEvent);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ClubEventExists(clubEvent.Id))
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
            ViewData["ClubId"] = new SelectList(_context.Clubs, "Id", "Id", clubEvent.ClubId);
            ViewData["State"] = new SelectList(_context.EventStates, "Id", "Id", clubEvent.State);
            return View(clubEvent);
        }

        // GET: ClubEvents/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var clubEvent = await _context.ClubEvents
                .Include(c => c.Club)
                .Include(c => c.StateNavigation)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (clubEvent == null)
            {
                return NotFound();
            }

            return View(clubEvent);
        }

        // POST: ClubEvents/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var clubEvent = await _context.ClubEvents.FindAsync(id);
            if (clubEvent != null)
            {
                _context.ClubEvents.Remove(clubEvent);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool ClubEventExists(int id)
        {
            return _context.ClubEvents.Any(e => e.Id == id);
        }

        [Route("api/Event")]
        [HttpPost]
        public async Task<IActionResult> PostEvent([FromBody] ClubEvent clubEvent)
        {
            if (ModelState.IsValid)
            {
                _context.ClubEvents.Add(clubEvent);
                await _context.SaveChangesAsync();
                return Ok(clubEvent);
            }
            return BadRequest("Invalid event data");
        }

        [Route("api/Events/{clubId}")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<ClubEvent>>> GetEvents(int clubId)
        {
            var events = await _context.ClubEvents.Where(x=>x.ClubId==clubId).ToListAsync();
            if (events == null || !events.Any())
            {
                return NotFound();
            }
            return events;
        }

        [Route("api/Event/{id}")]
        [HttpGet]
        public async Task<ActionResult<ClubEvent>> GetEvent(int id)
        {
            var ClubEvent = await _context.ClubEvents.FindAsync(id);
            if (ClubEvent == null)
            {
                return NotFound();
            }
            return ClubEvent;
        }

        [Authorize]
        [Route("api/Event")]
        [HttpDelete]
        public async Task<ActionResult<bool>> DeleteEvent([FromQuery]int id)
        {
            var clubEvent = await _context.ClubEvents.FindAsync(id);
            if (clubEvent == null)
            {
                return false;
            }
            _context.ClubEvents.Remove(clubEvent);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}
