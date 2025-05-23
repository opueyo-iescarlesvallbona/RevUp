﻿using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Hosting;
using Microsoft.IdentityModel.Tokens;
using RevupAPI.Models;

namespace RevupAPI.Controllers
{
    public class MembersController : Controller
    {
        private readonly RevupContext _context;

        public MembersController(RevupContext context)
        {
            _context = context;
        }

        // GET: Members
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.Members.Include(m => m.Gender).Include(m => m.Location);
            return View(await revupContext.ToListAsync());
        }

        // GET: Members/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var member = await _context.Members
                .Include(m => m.Gender)
                .Include(m => m.Location)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (member == null)
            {
                return NotFound();
            }

            return View(member);
        }

        // GET: Members/Create
        public IActionResult Create()
        {
            ViewData["GenderId"] = new SelectList(_context.Genders, "Id", "Id");
            ViewData["LocationId"] = new SelectList(_context.MemberLocations, "Id", "Id");
            return View();
        }

        // POST: Members/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name,Membername,Experience,Email,GenderId,LocationId,DateOfBirth,LoginDate,Description,ProfilePicture,Password")] Member member)
        {
            if (ModelState.IsValid)
            {
                _context.Add(member);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["GenderId"] = new SelectList(_context.Genders, "Id", "Id", member.GenderId);
            ViewData["LocationId"] = new SelectList(_context.MemberLocations, "Id", "Id", member.LocationId);
            return View(member);
        }

        // GET: Members/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var member = await _context.Members.FindAsync(id);
            if (member == null)
            {
                return NotFound();
            }
            ViewData["GenderId"] = new SelectList(_context.Genders, "Id", "Id", member.GenderId);
            ViewData["LocationId"] = new SelectList(_context.MemberLocations, "Id", "Id", member.LocationId);
            return View(member);
        }

        // POST: Members/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name,Membername,Experience,Email,GenderId,LocationId,DateOfBirth,LoginDate,Description,ProfilePicture,Password")] Member member)
        {
            if (id != member.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(member);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!MemberExists(member.Id))
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
            ViewData["GenderId"] = new SelectList(_context.Genders, "Id", "Id", member.GenderId);
            ViewData["LocationId"] = new SelectList(_context.MemberLocations, "Id", "Id", member.LocationId);
            return View(member);
        }

        // GET: Members/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var member = await _context.Members
                .Include(m => m.Gender)
                .Include(m => m.Location)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (member == null)
            {
                return NotFound();
            }

            return View(member);
        }

        // POST: Members/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var member = await _context.Members.FindAsync(id);
            if (member != null)
            {
                _context.Members.Remove(member);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }
         
        private bool MemberExists(int id)
        {
            return _context.Members.Any(e => e.Id == id);
        }

        [Route("api/MemberById")]
        [HttpGet]
        public async Task<ActionResult<Member>> GetMemberById([FromQuery]int id)
        {
            var member = await _context.Members.FindAsync(id);
            if (member == null)
            {
                return NotFound();
            }
            return member;
        }

        [Authorize]
        [Route("api/Member")]
        [HttpPut]
        public async Task<ActionResult<Member>> UpdateMember([FromForm] IFormFile? image, [FromForm] string member)
        {
            if (member == null)
            {
                return BadRequest("Invalid member");
            }
            var memberObj = Newtonsoft.Json.JsonConvert.DeserializeObject<Member>(member);
            if (memberObj == null)
            {
                return BadRequest("Invalid member data");
            }
            if (image != null)
            {
                try
                {
                    string path = GeneralController.UploadImage(image, memberObj);
                    memberObj.ProfilePicture = path;
                }
                catch { }
            }
            _context.Entry(memberObj).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!MemberExists(memberObj.Id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return Ok(memberObj);
        }

        [Route("api/Member")]
        [HttpPost]
        public async Task<ActionResult<Member>> PostMember([FromForm] IFormFile? image, [FromForm] string member)
        {           
            if (member == null)
            {
                return BadRequest("Invalid member");
            }
            Member? memberObj = null;
            try
            {
                memberObj = Newtonsoft.Json.JsonConvert.DeserializeObject<Member>(member);
            }catch(Exception e)
            {
                return BadRequest("Invalid member data: " + e.Message);
            }
            if (memberObj == null)
            {
                return BadRequest("Invalid member data");
            }
            if (memberObj.Password != null)
            {
                string password = memberObj.Password;

                string hashedPassword = BCrypt.Net.BCrypt.HashPassword(password);

                memberObj.Password = hashedPassword;
            }


            if (image != null)
            {
                try
                {
                    string path = GeneralController.UploadImage(image, memberObj);
                    memberObj.ProfilePicture = path;
                }
                catch { }
            }
            try
            {
                _context.Members.Add(memberObj);
                await _context.SaveChangesAsync();
            }
            catch
            {
                return BadRequest("Error saving member data");
            }
            
            return Ok(memberObj);
        }

        [Route("/api/login")]
        [HttpGet]
        public async Task<ActionResult<String>> Login([FromQuery] string memberName, [FromQuery] string password)
        {
            var member = await _context.Members.FirstOrDefaultAsync(x => x.Membername.Equals(memberName));
            if (member == null)
            {
                return NotFound();
            }
            if (BCrypt.Net.BCrypt.Verify(password, member.Password))
            {
                String token = GenerateJwtToken(member);
                if (token!=null)
                {
                    return Ok(token);
                }
            }
            return Unauthorized(null);
        }

        private string GenerateJwtToken(Member member)
        {
            string pass = Environment.GetEnvironmentVariable("TOKEN");
            if(string.IsNullOrEmpty(pass))
            {
                return null;
            }
            var claims = new[]
            {
                new Claim(ClaimTypes.NameIdentifier, member.Id.ToString())
            };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(pass));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                issuer: "RevUp",
                audience: "RevUpApp",
                claims: claims,
                expires: DateTime.Now.AddHours(24),
                signingCredentials: creds);

            var finalToken = "";

            try
            {
                finalToken = new JwtSecurityTokenHandler().WriteToken(token);
            }
            catch
            {
            }
            return finalToken;
        }

        [Authorize]
        [Route("api/Members")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Member>>> GetMembersByName([FromQuery]string memberName)
        {
            List <Member> members = new List<Member>();
            if (memberName == null)
            {
                members = await _context.Members.ToListAsync();
            }
            else
            {
                members = await _context.Members.Where(x => x.Membername.Contains(memberName)).ToListAsync();
            }
                
            if (members == null || !members.Any())
            {
                return NotFound();
            }
            return members;
        }

        [Authorize]
        [Route("api/MemberByCar")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Member>>> GetMemberByCar([FromQuery] string carName)
        {
            var members = await _context.Members
                .Include(m => m.Cars)
                .Where(m => m.Cars.Any(c => c.Model.ModelName.Contains(carName)))
                .ToListAsync();
            if (members == null || !members.Any())
            {
                return NotFound();
            }
            return members;
        }

        [Route("api/MemberFriends")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Member>>> GetMemberFriends([FromQuery] int id)
        {
            var member = await _context.Members.Where(x => x.Id == id).FirstOrDefaultAsync();


            List<Member> friends = new List<Member>();
            if (member != null)
            {
                var friendIds = await _context.MemberRelations.Where(x => x.MemberId1 == member.Id && x.State.Name.Equals("Friend")).Select(x => x.MemberId2).ToListAsync();
                friends = _context.Members.Where(x => friendIds.Contains(x.Id)).ToList();
            }
            return friends;
        }

        [Route("api/MemberExists")]
        [HttpGet]
        public async Task<ActionResult<bool>> MemberExists([FromQuery]string memberName)
        {

            var member = await _context.Members.Where(x=>x.Membername.Equals(memberName)).FirstOrDefaultAsync();
            if (member != null)
            {
                return true;
            }
            return false;
        }

        [Authorize]
        [Route("api/MemberByName")]
        [HttpGet]
        public async Task<ActionResult<Member>> GetMemberByName([FromQuery] string memberName)
        {
            var member = await _context.Members.Where(x => x.Membername.Equals(memberName)).FirstOrDefaultAsync();
            if (member == null)
            {
                return NotFound();
            }
            return member;
        }

        [Authorize]
        [Route("api/MemberClubs")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Club>>> GetClubsByMember([FromQuery] int id)
        {
            var member = await _context.Members.FindAsync(id);

            var clubs = await _context.MemberClubs.Where(x=>x.MemberId==member.Id).Select(x => x.Club).ToListAsync();

            if (clubs == null || !clubs.Any())
            {
                return NotFound();
            }
            return clubs;
        }
    }
}
