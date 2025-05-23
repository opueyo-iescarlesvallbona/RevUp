﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using RevupAPI.Models;

namespace RevupAPI.Controllers
{
    public class CarsController : Controller
    {
        private readonly RevupContext _context;

        public CarsController(RevupContext context)
        {
            _context = context;
        }

        // GET: Cars
        public async Task<IActionResult> Index()
        {
            var revupContext = _context.Cars.Include(c => c.Member).Include(c => c.Model);
            return View(await revupContext.ToListAsync());
        }

        // GET: Cars/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var car = await _context.Cars
                .Include(c => c.Member)
                .Include(c => c.Model)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (car == null)
            {
                return NotFound();
            }

            return View(car);
        }

        // GET: Cars/Create
        public IActionResult Create()
        {
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id");
            ViewData["ModelId"] = new SelectList(_context.Models, "Id", "Id");
            return View();
        }

        // POST: Cars/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,MemberId,ModelId,ModelYear,HorsePower,Description,Picture")] Car car)
        {
            if (ModelState.IsValid)
            {
                _context.Add(car);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", car.MemberId);
            ViewData["ModelId"] = new SelectList(_context.Models, "Id", "Id", car.ModelId);
            return View(car);
        }

        // GET: Cars/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var car = await _context.Cars.FindAsync(id);
            if (car == null)
            {
                return NotFound();
            }
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", car.MemberId);
            ViewData["ModelId"] = new SelectList(_context.Models, "Id", "Id", car.ModelId);
            return View(car);
        }

        // POST: Cars/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,MemberId,ModelId,ModelYear,HorsePower,Description,Picture")] Car car)
        {
            if (id != car.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(car);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!CarExists(car.Id))
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
            ViewData["MemberId"] = new SelectList(_context.Members, "Id", "Id", car.MemberId);
            ViewData["ModelId"] = new SelectList(_context.Models, "Id", "Id", car.ModelId);
            return View(car);
        }

        // GET: Cars/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var car = await _context.Cars
                .Include(c => c.Member)
                .Include(c => c.Model)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (car == null)
            {
                return NotFound();
            }

            return View(car);
        }

        // POST: Cars/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var car = await _context.Cars.FindAsync(id);
            if (car != null)
            {
                _context.Cars.Remove(car);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool CarExists(int id)
        {
            return _context.Cars.Any(e => e.Id == id);
        }

        [Route("api/Car")]
        [HttpPost]
        public async Task<ActionResult<Car>> PostCar([FromForm] IFormFile? image, [FromForm] string car)
        {
            if (car == null)
            {
                return BadRequest("Invalid car");
            }
            var carObj = Newtonsoft.Json.JsonConvert.DeserializeObject<Car>(car);
            if (carObj == null)
            {
                return BadRequest("Invalid car data");
            }
            try
            {
                var afterCar = _context.Cars.Add(carObj);
                await _context.SaveChangesAsync();
                if (image != null)
                {
                    try
                    {
                        string path = GeneralController.UploadImage(image, afterCar.Entity);
                        _context.Database.ExecuteSqlRaw("UPDATE car SET picture = {0} WHERE id = {1}", path, afterCar.Entity.Id);
                        carObj.Picture = path;
                    }
                    catch { }
                }
            }
            catch
            {
                return BadRequest("Error saving car");
            }
            return Ok(carObj);
        }

        [Route("api/Cars")]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Car>>> GetCarsByMemberId([FromQuery]int memberId)
        {
            var cars = await _context.Cars.Where(c => c.MemberId == memberId).ToListAsync();
            if (cars == null || !cars.Any())
            {
                return NotFound();
            }
            return Ok(cars);
        }

        [Route("api/Car")]
        [HttpPut]
        public async Task<ActionResult<Car>> PutCar([FromForm] string car, [FromForm] IFormFile? image)
        {
            if (car == null)
            {
                return BadRequest();
            }
            var carObj = JsonConvert.DeserializeObject<Car>(car);
            if (carObj == null)
            {
                return BadRequest("Invalid car data");
            }
            if (image != null)
            {
                try
                {
                    string path = GeneralController.UploadImage(image, carObj);
                    carObj.Picture = path;
                }
                catch { }
            }
            _context.Entry(carObj).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CarExists(carObj.Id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }
            return Ok(carObj);
        }

        [Authorize]
        [Route("api/Car")]
        [HttpDelete]
        public async Task<ActionResult<bool>> DeleteCar(int id)
        {
            var car = await _context.Cars.FindAsync(id);
            if (car == null)
            {
                return NotFound(false);
            }
            _context.Cars.Remove(car);
            await _context.SaveChangesAsync();
            return Ok(true);
        }
    }
}
