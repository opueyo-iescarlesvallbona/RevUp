using Microsoft.AspNetCore.Mvc;
using RevupAPI.Models;
using System.IO;
using System.Security.Cryptography;

namespace RevupAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
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

        // Este método obtiene la imagen de un usuario por su ID
        [HttpGet("GetImage/{path}")]
        public IActionResult GetImage(String path)
        {
            // Obtener el nombre del archivo de la imagen
            var imageFileName = path;

            if (string.IsNullOrEmpty(imageFileName))
                return NotFound("Image not found");

            // Construir la ruta completa de la imagen
            var imagePath = Path.Combine(_imagesFolderPath, imageFileName);

            // Verificar si la imagen existe en la ruta
            if (!System.IO.File.Exists(imagePath))
                return NotFound("Image file not found");

            // Abrir la imagen y devolverla en la respuesta
            var imageFile = System.IO.File.OpenRead(imagePath);

            // Aquí puedes especificar el tipo de contenido de la imagen (por ejemplo, "image/jpeg", "image/png")
            return File(imageFile, "image/jpeg");
        }
    }
}
