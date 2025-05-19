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
    public class MessagesController : Controller
    {
        private readonly RevupContext _context;

        public MessagesController(RevupContext context)
        {
            _context = context;
        }

        // GET: Messages
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.Messages.Include(m => m.Receiver).Include(m => m.Sender).Include(m => m.State);
            return View(await revupContext.ToListAsync());
        }

        // GET: Messages/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var message = await _context.Messages
                .Include(m => m.Receiver)
                .Include(m => m.Sender)
                .Include(m => m.State)
                .FirstOrDefaultAsync(m => m.SenderId == id);
            if (message == null)
            {
                return NotFound();
            }

            return View(message);
        }

        // GET: Messages/Create
        public IActionResult Create()
        {
            ViewData["ReceiverId"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["SenderId"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["StateId"] = new SelectList(_context.MessageStates, "Id", "Id");
            return View();
        }

        // POST: Messages/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("SenderId,ReceiverId,Datetime,ContentMessage,StateId")] Message message)
        {
            if (ModelState.IsValid)
            {
                _context.Add(message);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["ReceiverId"] = new SelectList(_context.Members, "Id", "Id", message.ReceiverId);
            ViewData["SenderId"] = new SelectList(_context.Members, "Id", "Id", message.SenderId);
            ViewData["StateId"] = new SelectList(_context.MessageStates, "Id", "Id", message.StateId);
            return View(message);
        }

        // GET: Messages/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var message = await _context.Messages.FindAsync(id);
            if (message == null)
            {
                return NotFound();
            }
            ViewData["ReceiverId"] = new SelectList(_context.Members, "Id", "Id", message.ReceiverId);
            ViewData["SenderId"] = new SelectList(_context.Members, "Id", "Id", message.SenderId);
            ViewData["StateId"] = new SelectList(_context.MessageStates, "Id", "Id", message.StateId);
            return View(message);
        }

        // POST: Messages/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("SenderId,ReceiverId,Datetime,ContentMessage,StateId")] Message message)
        {
            if (id != message.SenderId)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(message);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!MessageExists(message.SenderId))
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
            ViewData["ReceiverId"] = new SelectList(_context.Members, "Id", "Id", message.ReceiverId);
            ViewData["SenderId"] = new SelectList(_context.Members, "Id", "Id", message.SenderId);
            ViewData["StateId"] = new SelectList(_context.MessageStates, "Id", "Id", message.StateId);
            return View(message);
        }

        // GET: Messages/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var message = await _context.Messages
                .Include(m => m.Receiver)
                .Include(m => m.Sender)
                .Include(m => m.State)
                .FirstOrDefaultAsync(m => m.SenderId == id);
            if (message == null)
            {
                return NotFound();
            }

            return View(message);
        }

        // POST: Messages/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var message = await _context.Messages.FindAsync(id);
            if (message != null)
            {
                _context.Messages.Remove(message);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool MessageExists(int id)
        {
            return _context.Messages.Any(e => e.SenderId == id);
        }

        [Authorize]
        [Route("api/OldMessages")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<MemberLocation>>> GetGenderById([FromQuery] int senderId, int receiverId)
        {
            var messages = await _context.Messages.Where(x => (x.SenderId == senderId && x.ReceiverId == receiverId)||
            x.SenderId==receiverId && x.ReceiverId==senderId).OrderByDescending(x=>x.Datetime).ToListAsync();
            if (messages == null)
            {
                return NotFound();
            }

            return Ok(messages);
        }
    }
}
