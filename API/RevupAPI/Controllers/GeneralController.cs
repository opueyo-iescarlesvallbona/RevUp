using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using RevupAPI.Models;
using System.IO;
using System.Security.Cryptography;

namespace RevupAPI.Controllers
{
    //[Route("api/[controller]")]
    //[ApiController]
    public class GeneralController : ControllerBase
    {
        private readonly RevupContext _context;
        private readonly string _imagesFolderPath;

        // Constructor
        public GeneralController(RevupContext context, IConfiguration configuration)
        {
            _context = context;
            _imagesFolderPath = "C:\\Users\\cv\\Downloads\\";
        }

        //[Authorize]
        [Route("api/GetImage")]
        [HttpGet]
        public IActionResult GetImage([FromQuery] String path)
        {
            var imageFileName = path;

            if (string.IsNullOrEmpty(imageFileName))
                return NotFound("Image not found");

            var imagePath = Path.Combine(_imagesFolderPath, imageFileName);

            if (!System.IO.File.Exists(imagePath))
                return NotFound("Image file not found");

            var imageFile = System.IO.File.OpenRead(imagePath);

            string extension = Path.GetExtension(imagePath).ToLowerInvariant();
            string contentType = extension switch
            {
                ".jpg" or ".jpeg" => "image/jpeg",
                ".png" => "image/png",
                ".gif" => "image/gif",
                ".bmp" => "image/bmp",
                ".webp" => "image/webp",
                _ => "application/octet-stream"
            };

            return File(imageFile, contentType);
        }

        public static string UploadImage(IFormFile imageFile, Object obj)
        {
            string _imagesFolderPath = "C:\\Users\\cv\\Downloads\\";
            if (imageFile == null || imageFile.Length == 0)
            {
                return "";
            }

            string fileType = Path.GetExtension(imageFile.FileName).ToLowerInvariant();

            //string dateTimeNow = DateTime.Now.ToString("yyyyMMdd_HHmmss");
            string imageFileName = "";
            string targetFolder = "";
            string pathSaved = "";
            switch (obj)
            {
                case Post post:
                    imageFileName = $"{post.Id}.{fileType}";
                    targetFolder = Path.Combine(_imagesFolderPath, "posts");
                    pathSaved = Path.Combine("posts", imageFileName);
                    break;
                case Member member:
                    imageFileName = $"{member.Id}.{fileType}";
                    targetFolder = Path.Combine(_imagesFolderPath, "members");
                    pathSaved = Path.Combine("members", imageFileName);
                    break;
                case Club club:
                    imageFileName = $"{club.Id}.{fileType}";
                    targetFolder = Path.Combine(_imagesFolderPath, "clubs");
                    pathSaved = Path.Combine("clubs", imageFileName);
                    break;
                case Car car:
                    imageFileName = $"{car.Id}.{fileType}";
                    targetFolder = Path.Combine(_imagesFolderPath, "cars");
                    pathSaved = Path.Combine("cars", imageFileName);
                    break;
                case ClubEvent clubEvent:
                    imageFileName = $"{clubEvent.Id}.{fileType}";
                    targetFolder = Path.Combine(_imagesFolderPath, "events");
                    pathSaved = Path.Combine("events", imageFileName);
                    break;
            }

            if (imageFileName.Equals("") || targetFolder.Equals(""))
            {
                return "";
            }

            if (!Directory.Exists(targetFolder))
            {
                Directory.CreateDirectory(targetFolder);
            }

            string filePath = Path.Combine(targetFolder, imageFileName);

            if (System.IO.File.Exists(filePath))
            {
                System.IO.File.Delete(filePath);
            }

            using (var fileStream = new FileStream(filePath, FileMode.Create))
            {
                imageFile.CopyTo(fileStream);
            }

            return pathSaved;
        }

        public static bool DeleteImage(string imageFileName, Object obj)
        {
            string _imagesFolderPath = "C:\\Users\\cv\\Downloads\\";
            string targetFolder = "";
            switch (obj)
            {
                case Post post:
                    targetFolder = Path.Combine(_imagesFolderPath, "posts");
                    break;
                case Member member:
                    targetFolder = Path.Combine(_imagesFolderPath, "members");
                    break;
                case Club club:
                    targetFolder = Path.Combine(_imagesFolderPath, "clubs");
                    break;
                case Car car:
                    targetFolder = Path.Combine(_imagesFolderPath, "cars");
                    break;
                case ClubEvent clubEvent:
                    targetFolder = Path.Combine(_imagesFolderPath, "events");
                    break;
            }
            if (targetFolder.Equals(""))
            {
                return false;
            }
            string filePath = Path.Combine(targetFolder, imageFileName);
            if (System.IO.File.Exists(filePath))
            {
                System.IO.File.Delete(filePath);
                return true;
            }
            return false;
        }
    }
}
