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

        [Authorize]
        [Route("api/Event")]
        [HttpPost]
        public async Task<ActionResult<ClubEvent>> PostEvent([FromForm] IFormFile? image, [FromForm] string clubEvent)
        {
            if(clubEvent == null)
            {
                return BadRequest("Invalid event");
            }
            var clubEventObj = Newtonsoft.Json.JsonConvert.DeserializeObject<ClubEvent>(clubEvent);
            if (clubEventObj == null)
            {
                return BadRequest("Invalid event data");
            }

            try
            {
                var afterEvent = _context.ClubEvents.Add(clubEventObj);
                await _context.SaveChangesAsync();
                if (image!=null)
                {
                    try
                    {
                        string path = GeneralController.UploadImage(image, afterEvent.Entity);
                        _context.Database.ExecuteSqlRaw("UPDATE club_event SET picture = {0} WHERE id = {1}", path, afterEvent.Entity.Id);
                        clubEventObj.Picture = path;
                    }
                    catch { }
                }
            }
            catch
            {
                return BadRequest("Error saving event data");
            }
            return Ok(clubEventObj);
        }

        [Authorize]
        [Route("api/Events")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<ClubEvent>>> GetEvents([FromQuery]int clubId)
        {
            var events = await _context.ClubEvents.Where(x=>x.ClubId==clubId).ToListAsync();
            if (events == null || !events.Any())
            {
                return NotFound();
            }
            return events;
        }

        [Authorize]
        [Route("api/Event")]
        [HttpGet]
        public async Task<ActionResult<ClubEvent>> GetEvent([FromQuery]int id)
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

        [Authorize]
        [Route("api/Event")]
        [HttpPut]
        public async Task<ActionResult<ClubEvent>> UpdateEvent([FromForm] IFormFile? image, [FromForm] string clubEvent)
        {
            if (clubEvent == null)
            {
                return BadRequest("Invalid event");
            }
            var clubEventObj = Newtonsoft.Json.JsonConvert.DeserializeObject<ClubEvent>(clubEvent);
            if (clubEventObj == null)
            {
                return BadRequest("Invalid event data");
            }
            if (image != null)
            {
                try
                {
                    string path = GeneralController.UploadImage(image, clubEventObj);
                    clubEventObj.Picture = path;
                }
                catch { }
            }
            _context.Entry(clubEventObj).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ClubEventExists(clubEventObj.Id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return Ok(clubEventObj);
        }

        [Authorize]
        [Route("api/EventStateById")]
        [HttpGet]
        public async Task<ActionResult<EventState>> GetEventStateById([FromQuery] int id)
        {
            var eventState = await _context.EventStates.FindAsync(id);
            if (eventState == null)
            {
                return NotFound();
            }
            return eventState;
        }

        [Authorize]
        [Route("api/EventStates")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<EventState>>> GetEventStates()
        {
            var eventStates = await _context.EventStates.ToListAsync();
            if (eventStates == null || !eventStates.Any())
            {
                return NotFound();
            }
            return eventStates;
        }

    }
}
