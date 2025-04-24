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

        [HttpGet("GetImage/{path}")]
        public IActionResult GetImage(String path)
        {
            var imageFileName = path;

            if (string.IsNullOrEmpty(imageFileName))
                return NotFound("Image not found");

            var imagePath = Path.Combine(_imagesFolderPath, imageFileName);

            if (!System.IO.File.Exists(imagePath))
                return NotFound("Image file not found");

            var imageFile = System.IO.File.OpenRead(imagePath);

            // Aquí puedes especificar el tipo de contenido de la imagen (por ejemplo, "image/jpeg", "image/png")
            return File(imageFile, "image/jpeg");
        }

        [HttpPost("uploadImage")]
        public static string UploadImage(IFormFile imageFile, Object obj)
        {
            string _imagesFolderPath = "C:\\Users\\cv\\Downloads\\";
            if (imageFile == null || imageFile.Length == 0)
            {
                return "";
            }

            string dateTimeNow = DateTime.Now.ToString("yyyyMMdd_HHmmss");
            string imageFileName = "";
            string targetFolder = "";
            switch (obj)
            {
                case Post post:
                    imageFileName = $"{post.Id}.jpg";
                    targetFolder = Path.Combine(_imagesFolderPath, "posts");
                    
                    break;
                case Member member:
                    imageFileName = $"{member.Id}.jpg";
                    targetFolder = Path.Combine(_imagesFolderPath, "members");
                    break;
                case Club club:
                    imageFileName = $"{club.Id}.jpg";
                    targetFolder = Path.Combine(_imagesFolderPath, "clubs");
                    break;
                case Car car:
                    imageFileName = $"{car.Id}.jpg";
                    targetFolder = Path.Combine(_imagesFolderPath, "cars");
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

            using (var fileStream = new FileStream(filePath, FileMode.Create))
            {
                imageFile.CopyTo(fileStream);
            }

            return filePath;
        }
    }
}
