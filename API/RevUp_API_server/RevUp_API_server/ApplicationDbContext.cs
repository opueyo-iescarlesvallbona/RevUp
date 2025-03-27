using Microsoft.EntityFrameworkCore;
using RevUp_API_server.Models;

namespace RevUp_API_server
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options) { }

        public DbSet<Member> member { get; set; }
    }
}
