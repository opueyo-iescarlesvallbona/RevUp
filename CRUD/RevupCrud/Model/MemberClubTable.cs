using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RevupCrud.Model
{
    public class MemberClubTable
    {
        public int Id { get; set; }
        public string name { get; set; }
        public string role { get; set; }
        public DateTime join_date { get; set; }
    }
}
