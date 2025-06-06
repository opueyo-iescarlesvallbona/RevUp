﻿using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using RevupAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RevupAPI.Controllers
{
    public class PostsController : Controller
    {
        private readonly RevupContext _context;

        public PostsController(RevupContext context)
        {
            _context = context;
        }

        // GET: Posts
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.Posts.Include(p => p.Member).Include(p => p.PostTypeNavigation).Include(p => p.Route);
            return View(await revupContext.ToListAsync());
        }

        // GET: Posts/Details/5
        [Route("api/details/{id}")]
        [HttpGet]
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var post = await _context.Posts
                .Include(p => p.Member)
                .Include(p => p.PostTypeNavigation)
                .Include(p => p.Route)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (post == null)
            {
                return NotFound();
            }

            return View(post);
        }

        // GET: Posts/Create
        public IActionResult Create()
        {
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["PostType"] = new SelectList(_context.PostTypes, "Id", "Id");
            ViewData["RouteId"] = new SelectList(_context.Routes, "Id", "Id");
            return View();
        }

        // POST: Posts/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Title,PostType,Description,PostDate,Picture,Likes,Address,RouteId,MemberId,Comments")] Post post)
        {
            if (ModelState.IsValid)
            {
                _context.Add(post);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", post.MemberId);
            ViewData["PostType"] = new SelectList(_context.PostTypes, "Id", "Id", post.PostType);
            ViewData["RouteId"] = new SelectList(_context.Routes, "Id", "Id", post.RouteId);
            return View(post);
        }

        // GET: Posts/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var post = await _context.Posts.FindAsync(id);
            if (post == null)
            {
                return NotFound();
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", post.MemberId);
            ViewData["PostType"] = new SelectList(_context.PostTypes, "Id", "Id", post.PostType);
            ViewData["RouteId"] = new SelectList(_context.Routes, "Id", "Id", post.RouteId);
            return View(post);
        }

        // POST: Posts/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Title,PostType,Description,PostDate,Picture,Likes,Address,RouteId,MemberId,Comments")] Post post)
        {
            if (id != post.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(post);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!PostExists(post.Id))
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
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", post.MemberId);
            ViewData["PostType"] = new SelectList(_context.PostTypes, "Id", "Id", post.PostType);
            ViewData["RouteId"] = new SelectList(_context.Routes, "Id", "Id", post.RouteId);
            return View(post);
        }

        // GET: Posts/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var post = await _context.Posts
                .Include(p => p.Member)
                .Include(p => p.PostTypeNavigation)
                .Include(p => p.Route)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (post == null)
            {
                return NotFound();
            }

            return View(post);
        }

        // POST: Posts/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var post = await _context.Posts.FindAsync(id);
            if (post != null)
            {
                _context.Posts.Remove(post);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool PostExists(int id)
        {
            return _context.Posts.Any(e => e.Id == id);
        }

        [Route("api/PostsByLocationId")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Post>>> GetPostsByLocation([FromQuery] int location_id)
        {
            var posts = await _context.Posts.Where(x => x.LocationId==location_id).ToListAsync();

            if (posts == null || !posts.Any())
            {
                return NotFound();
            }
            else
            {
                posts = posts.OrderByDescending(x => x.PostDate).ToList();
            }
            return posts;
        }

        [Authorize]
        [Route("api/PostsByLikes")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Post>>> GetPostsByLikes()
        {
            var posts = await _context.Posts.OrderByDescending(x => x.Likes).ToListAsync();

            return posts;
        }

        [Authorize]
        [Route("api/PostsByMemberFriends")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Post>>> GetPostsByMemberFriends([FromQuery] int memberId)
        {
            var member = await _context.MemberRelations.Where(x=>x.MemberId1==memberId&&x.State.Name.Equals("Friend")).Select(x=>x.MemberId2).ToListAsync();

            if (member == null)
            {
                return NotFound();
            }
            var posts = await _context.Posts
                .Where(p => member.Contains(p.MemberId))
                .ToListAsync();
            if (posts == null || !posts.Any())
            {
                return NotFound();
            }
            else
            {
                posts = posts.OrderByDescending(x => x.PostDate).ToList();
            }

            return posts;
        }

        [Authorize]
        [Route("api/PostById")]
        [HttpGet]
        public async Task<ActionResult<Post>> GetPost([FromQuery] int id)
        {
            var post = await _context.Posts.FirstOrDefaultAsync(m => m.Id == id);
            if (post == null)
            {
                return NotFound();
            }
            return post;
        }

        [Route("api/Post")]
        [HttpPost]

        public async Task<ActionResult<Post>> PostPost([FromForm] IFormFile? image, [FromForm] string post)
        {
            if (post == null)
            {
                return BadRequest("Invalid post");
            }
            var settings = new JsonSerializerSettings
            {
                MissingMemberHandling = MissingMemberHandling.Ignore,
                NullValueHandling = NullValueHandling.Ignore,
                ContractResolver = new Newtonsoft.Json.Serialization.DefaultContractResolver
                {
                    NamingStrategy = new Newtonsoft.Json.Serialization.CamelCaseNamingStrategy()
                }
            };
            post = post.Replace("}", ", \"id\":\"0\"}");
            var postObj = Newtonsoft.Json.JsonConvert.DeserializeObject<Post>(post, settings);
            if (postObj == null)
            {
                return BadRequest("Invalid post object");
            }
            
            try
            {

                //_context.Database.ExecuteSqlRaw("", post);
                var afterPost = _context.Posts.Add(postObj);
                await _context.SaveChangesAsync();
                if (image != null)
                {
                    try
                    {
                        string path = GeneralController.UploadImage(image, afterPost.Entity);
                        _context.Database.ExecuteSqlRaw("UPDATE post SET picture = {0} WHERE id = {1}", path, afterPost.Entity.Id);
                        postObj.Picture = path;
                    }
                    catch { }
                }
            }
            catch
            {
                return BadRequest("Error saving post");
            }

            return Ok(postObj);
        }

        [Route("api/Post")]
        [HttpPut]
        public async Task<ActionResult<Post>> PutPost([FromForm] IFormFile? image, [FromForm] string post)
        {
            if(post == null)
            {
                return BadRequest("Invalid post");
            }
            var postObj = Newtonsoft.Json.JsonConvert.DeserializeObject<Post>(post);
            if (postObj == null)
            {
                return BadRequest("Invalid post object");
            }
            if (image != null)
            {
                try
                {
                    string path = GeneralController.UploadImage(image, postObj);
                    postObj.Picture = path;
                }
                catch { }
            }
            _context.Entry(postObj).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PostExists(postObj.Id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }
            return Ok(postObj);
        }

        [Route("api/Post")]
        [HttpDelete]
        public async Task<bool> DeletePost([FromQuery] int id)
        {
            var post = await _context.Posts.FindAsync(id);

            if (post == null)
            {
                return false;
            }

            var comments = await _context.PostComments.Where(c => c.PostId == id).ToListAsync();

            if (comments != null)
            {
                _context.PostComments.RemoveRange(comments);
            }

            _context.Posts.Remove(post);
            await _context.SaveChangesAsync();
            return true;
        }

        [Route("api/PostLike")]
        [HttpPost]
        public async Task<IActionResult> LikePost([FromQuery]int memberId, [FromQuery]int postId)
        {
            var post = await _context.Posts.FindAsync(postId);
            var member = await _context.Members.FindAsync(memberId);
            if (post == null || member == null)
            {
                return NotFound();
            }
            post.Members.Add(member);
            await _context.SaveChangesAsync();
            return Ok(post);
        }

        [Route("api/PostUnLike")]
        [HttpDelete]
        public async Task<IActionResult> UnlikePost([FromQuery] int memberId, [FromQuery] int postId)
        {
            var post = await _context.Posts.FindAsync(postId);
            var member = await _context.Members.FindAsync(memberId);
            if (post == null || member == null)
            {
                return NotFound();
            }

            await _context.Database.ExecuteSqlRawAsync("DELETE FROM post_member_like WHERE post_id = {0} AND member_id = {1}", postId, memberId);
            
            //post.Members.Remove(member);
            await _context.SaveChangesAsync();
            return Ok(post);
        }

        [Authorize]
        [Route("api/PostIsLikedByMember")]
        [HttpGet]
        public async Task<ActionResult<bool>> IsPostLiked([FromQuery] int memberId, [FromQuery] int postId)
        {
            var post = await _context.Posts.FindAsync(postId);
            var member = await _context.Members.FindAsync(memberId);
            
            if (post == null || member == null)
            {
                return NotFound();
            }
            
            var members = _context.Posts.Where(x => x.Id == postId).SelectMany(x => x.Members).ToList();
            var isLiked = members.Contains(member);
            return Ok(isLiked);
        }

        [Authorize]
        [Route("api/PostsByMemberId")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Post>>> GetPostsByMemberId([FromQuery] int memberId)
        {
            var posts = await _context.Posts.Where(x => x.MemberId == memberId).ToListAsync();
            if (posts == null || !posts.Any())
            {
                return NotFound();
            }
            return posts;
        }
    }
}
