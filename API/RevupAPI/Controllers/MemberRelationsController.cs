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
    public class MemberRelationsController : Controller
    {
        private readonly RevupContext _context;

        public MemberRelationsController(RevupContext context)
        {
            _context = context;
        }

        // GET: MemberRelations
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.MemberRelations.Include(m => m.MemberId1Navigation).Include(m => m.MemberId2Navigation).Include(m => m.State);
            return View(await revupContext.ToListAsync());
        }

        // GET: MemberRelations/Create
        public IActionResult Create()
        {
            ViewData["MemberId1"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["MemberId2"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["StateId"] = new SelectList(_context.RelationStates, "Id", "Id");
            return View();
        }

        // POST: MemberRelations/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("MemberId1,MemberId2,StateId")] MemberRelation memberRelation)
        {
            if (ModelState.IsValid)
            {
                _context.Add(memberRelation);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["MemberId1"] = new SelectList(_context.Members, "Id", "Id", memberRelation.MemberId1);
            ViewData["MemberId2"] = new SelectList(_context.Members, "Id", "Id", memberRelation.MemberId2);
            ViewData["StateId"] = new SelectList(_context.RelationStates, "Id", "Id", memberRelation.StateId);
            return View(memberRelation);
        }

        // GET: MemberRelations/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var memberRelation = await _context.MemberRelations.FindAsync(id);
            if (memberRelation == null)
            {
                return NotFound();
            }
            ViewData["MemberId1"] = new SelectList(_context.Members, "Id", "Id", memberRelation.MemberId1);
            ViewData["MemberId2"] = new SelectList(_context.Members, "Id", "Id", memberRelation.MemberId2);
            ViewData["StateId"] = new SelectList(_context.RelationStates, "Id", "Id", memberRelation.StateId);
            return View(memberRelation);
        }

        // POST: MemberRelations/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("MemberId1,MemberId2,StateId")] MemberRelation memberRelation)
        {
            if (id != memberRelation.MemberId1)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(memberRelation);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!MemberRelationExists(memberRelation.MemberId1))
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
            ViewData["MemberId1"] = new SelectList(_context.Members, "Id", "Id", memberRelation.MemberId1);
            ViewData["MemberId2"] = new SelectList(_context.Members, "Id", "Id", memberRelation.MemberId2);
            ViewData["StateId"] = new SelectList(_context.RelationStates, "Id", "Id", memberRelation.StateId);
            return View(memberRelation);
        }

        // GET: MemberRelations/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var memberRelation = await _context.MemberRelations
                .Include(m => m.MemberId1Navigation)
                .Include(m => m.MemberId2Navigation)
                .Include(m => m.State)
                .FirstOrDefaultAsync(m => m.MemberId1 == id);
            if (memberRelation == null)
            {
                return NotFound();
            }

            return View(memberRelation);
        }

        // POST: MemberRelations/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var memberRelation = await _context.MemberRelations.FindAsync(id);
            if (memberRelation != null)
            {
                _context.MemberRelations.Remove(memberRelation);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool MemberRelationExists(int id)
        {
            return _context.MemberRelations.Any(e => e.MemberId1 == id);
        }


        [Authorize]
        [Route("api/MemberRelationsByMemberId")]
        [HttpGet]
        public async Task<IActionResult> GetMemberRelationsByMemberId([FromQuery]int id)
        {
            var memberRelation = await _context.MemberRelations
                .Include(m => m.MemberId1Navigation)
                .Include(m => m.MemberId2Navigation)
                .Include(m => m.State)
                .FirstOrDefaultAsync(m => m.MemberId1 == id);
            if (memberRelation == null)
            {
                return NotFound();
            }

            return View(memberRelation);
        }

        [Authorize]
        [Route("api/MemberRelation")]
        [HttpPost]
        public async Task<ActionResult<MemberRelation>> PostMemberRelation([FromBody] MemberRelation memberRelation)
        {
            if (ModelState.IsValid)
            {
                _context.Add(memberRelation);
                await _context.SaveChangesAsync();
                return memberRelation;
            }
            return BadRequest("Incorrect member relation data");
        }

        [Authorize]
        [Route("api/MemberRelation")]
        [HttpPut]
        public async Task<ActionResult<MemberRelation>> PutMemberRelation([FromBody] MemberRelation memberRelation)
        {
            if (ModelState.IsValid)
            {
                _context.Update(memberRelation);
                await _context.SaveChangesAsync();
                return memberRelation;
            }
            return BadRequest("Incorrect member relation data");
        }

        [Authorize]
        [Route("api/MemberRelation")]
        [HttpDelete]
        public async Task<ActionResult<MemberRelation>> DeleteMemberRelation([FromQuery] int id)
        {
            var memberRelation = await _context.MemberRelations.FindAsync(id);
            if (memberRelation == null)
            {
                return NotFound();
            }
            _context.MemberRelations.Remove(memberRelation);
            await _context.SaveChangesAsync();
            return memberRelation;
        }

    }
}
