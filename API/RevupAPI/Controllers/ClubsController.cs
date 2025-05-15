using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;
using RevupAPI.Models;

namespace RevupAPI.Controllers
{
    public class ClubsController : Controller
    {
        private readonly RevupContext _context;

        public ClubsController(RevupContext context)
        {
            _context = context;
        }

        // GET: Clubs
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.Clubs.Include(c => c.FounderNavigation);
            return View(await revupContext.ToListAsync());
        }

        // GET: Clubs/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var club = await _context.Clubs
                .Include(c => c.FounderNavigation)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (club == null)
            {
                return NotFound();
            }

            return View(club);
        }

        // GET: Clubs/Create
        public IActionResult Create()
        {
            ViewData["Founder"] = new SelectList(_context.Members, "Id", "Id");
            return View();
        }

        // POST: Clubs/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name,Founder,Description,Picture,CreationDate")] Club club)
        {
            if (ModelState.IsValid)
            {
                _context.Add(club);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["Founder"] = new SelectList(_context.Members, "Id", "Id", club.Founder);
            return View(club);
        }

        // GET: Clubs/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var club = await _context.Clubs.FindAsync(id);
            if (club == null)
            {
                return NotFound();
            }
            ViewData["Founder"] = new SelectList(_context.Members, "Id", "Id", club.Founder);
            return View(club);
        }

        // POST: Clubs/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name,Founder,Description,Picture,CreationDate")] Club club)
        {
            if (id != club.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(club);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ClubExists(club.Id))
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
            ViewData["Founder"] = new SelectList(_context.Members, "Id", "Id", club.Founder);
            return View(club);
        }

        // GET: Clubs/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var club = await _context.Clubs
                .Include(c => c.FounderNavigation)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (club == null)
            {
                return NotFound();
            }

            return View(club);
        }

        // POST: Clubs/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var club = await _context.Clubs.FindAsync(id);
            if (club != null)
            {
                _context.Clubs.Remove(club);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool ClubExists(int id)
        {
            return _context.Clubs.Any(e => e.Id == id);
        }

        [Authorize]
        [Route("api/Clubs")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Club>>> GetMembersByName([FromQuery] string clubName)
        {
            List<Club> clubs = new List<Club>();
            if (string.IsNullOrEmpty(clubName))
            {
                clubs = await _context.Clubs.ToListAsync();
            }
            else
            {
                clubs = await _context.Clubs.Where(x => x.Name.Contains(clubName)).ToListAsync();
            }

            if (clubs == null || !clubs.Any())
            {
                return NotFound();
            }
            return clubs;
        }

        [Authorize]
        [Route("api/ClubById")]
        [HttpGet]
        public async Task<ActionResult<Club>> ClubById([FromQuery] int id)
        {
            var club = await _context.Clubs.Where(x=>x.Id==id).FirstOrDefaultAsync();
            if (club == null)
            {
                return NotFound();
            }
            return club;
        }


        [Authorize]
        [Route("api/Club")]
        [HttpPost]
        public async Task<ActionResult<Club>> PostEvent([FromForm] IFormFile? image, [FromForm] string club)
        {
            if(club == null)
            {
                return BadRequest("Invalid club");
            }
            var clubObj = Newtonsoft.Json.JsonConvert.DeserializeObject<Club>(club);
            if(clubObj == null)
            {
                return BadRequest("Invalid club data");
            }
            if(image != null)
            {
                try
                {
                    string path = GeneralController.UploadImage(image, clubObj);
                    clubObj.Picture = path;
                }
                catch { }
            }
            try
            {
                _context.Clubs.Add(clubObj);
                await _context.SaveChangesAsync();
            }
            catch
            {
                return BadRequest("Error saving club");
            }

            return Ok(clubObj);
        }

        [Authorize]
        [Route("api/Club")]
        [HttpPut]
        public async Task<ActionResult<Club>> UpdateMember([FromForm] IFormFile? image, [FromForm] string club)
        {
            if (club == null)
            {
                return BadRequest("Invalid club");
            }
            var clubObj = Newtonsoft.Json.JsonConvert.DeserializeObject<Club>(club);
            if (clubObj == null)
            {
                return BadRequest("Invalid club data");
            }
            if (image != null)
            {
                try
                {
                    string path = GeneralController.UploadImage(image, clubObj);
                    clubObj.Picture = path;
                }
                catch { }
            }
            _context.Entry(clubObj).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ClubExists(clubObj.Id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return Ok(clubObj);
        }

        [Authorize]
        [Route("api/ClubMembers")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Member>>> GetMembersByClub([FromQuery] int clubId)
        {
            var members = await _context.MemberClubs.Where(mc => mc.ClubId == clubId).Select(mc => mc.Member).ToListAsync();

            if (members == null || !members.Any())
            {
                return NotFound();
            }
            return members;
        }

        [Authorize]
        [Route("api/MemberClubRoleById")]
        [HttpGet]
        public async Task<ActionResult<MemberClubRole>> GetMemberClubRoleById([FromQuery] int clubId, [FromQuery] int memberId)
        {
            var clubF = await _context.Clubs.Where(x => x.Id == clubId).FirstOrDefaultAsync();
            if(clubF.Founder.Equals(memberId))
            {
                var founderRole = await _context.MemberClubRoles.Where(x => x.Name == "Founder").FirstOrDefaultAsync();
                if (founderRole == null)
                {
                    return NotFound();
                }
                return founderRole;
            }

            var member_club = await _context.MemberClubs.Where(x => x.ClubId == clubId && x.MemberId == memberId).FirstOrDefaultAsync();

            
            if (member_club == null)
            {
                return NotFound();
            }
            var role = await _context.MemberClubRoles.Where(x => x.Id == member_club.RoleType).FirstOrDefaultAsync();
            return Ok(role);
        }

        [Authorize]
        [Route("api/MemberClubRoles")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<MemberClubRole>>> GetMemberClubRoles()
        {
            var memberRole = await _context.MemberClubRoles.ToListAsync();
            if (memberRole == null|| !memberRole.Any())
            {
                return NotFound();
            }
            return memberRole;
        }


        [Authorize]
        [Route("api/MemberClub")]
        [HttpPost]
        public async Task<ActionResult<bool>> PostMemberClub([FromBody] MemberClub memberClub)
        {
            try {
                _context.MemberClubs.Add(memberClub);
                await _context.SaveChangesAsync();
                return Ok(memberClub);
            }
            catch
            {
                return BadRequest("Invalid member club data");
            }
        }

        [Authorize]
        [Route("api/MemberClub")]
        [HttpPut]
        public async Task<ActionResult<bool>> PutMemberClub([FromBody] MemberClub memberClub)
        {
            var existingMemberClub = await _context.MemberClubs.Where(x=>x.ClubId==memberClub.ClubId&&x.MemberId==memberClub.MemberId).FirstOrDefaultAsync();

            if (existingMemberClub != null)
            {
                existingMemberClub.RoleType = memberClub.RoleType;
                _context.Entry(existingMemberClub).State = EntityState.Modified;
                _context.SaveChanges();
                return Ok(memberClub);
            }
            else
            {
                return NotFound();
            }
        }

        [Authorize]
        [Route("api/MemberClub")]
        [HttpDelete]
        public async Task<ActionResult<bool>> DeleteMemberClub([FromQuery] int memberId, [FromQuery] int clubId)
        {
            var MemberClub = await _context.MemberClubs.Where(x => x.ClubId == clubId && x.MemberId == memberId).FirstOrDefaultAsync();
            if (MemberClub != null)
            {
                _context.MemberClubs.Remove(MemberClub);
                await _context.SaveChangesAsync();
                return Ok(true);
            }
            else
            {
                return NotFound(false);
            }
        }

    }
}
