using RepositoriRevUp.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RevupCrud.Model
{
    public class MemberRelationTable
    {
        public int Id { get; set; }
        public String Friend { get; set; }        
        public String State { get; set; }
        
    }
}
