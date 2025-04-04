using RepositoriRevUp.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RevupCrud.Model
{
    public class MemberCarTable
    {
        public int Id { get; set; }        
        public String Brand { get; set; }
        public String Model { get; set; }
        public Nullable<int> Year { get; set; }
        public Nullable<double> HP { get; set; }        
    }

}
