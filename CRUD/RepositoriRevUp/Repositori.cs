using RepositoriRevUp.Model;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace RepositoriRevUp
{
    public class Repositori
    {
        public static string password = "";
        public static revupEntities db;
        public static void dbConnect()
        {
            db = new revupEntities(password);
            
        }
    }
}
