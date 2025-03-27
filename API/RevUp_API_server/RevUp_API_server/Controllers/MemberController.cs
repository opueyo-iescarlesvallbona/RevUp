using Microsoft.AspNetCore.Mvc;
using RevUp_API_server.Models;

namespace RevUp_API_server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MemberController
    {
        private readonly PostgresContext context;

        public MemberController(PostgresContext context)
        {
            this.context = context;
        }

        [HttpGet]
        public IEnumerable<Member> Get()
        {
            return context.Members.ToList();
        }
    }
}
