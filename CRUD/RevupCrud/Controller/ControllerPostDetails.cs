using RepositoriRevUp.Model;
using RevupCrud.View;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RevupCrud.Controller
{
    public class ControllerPostDetails
    {
        public ControllerPostDetails(post post, ViewPostDetails f)
        {
            f.Show();
        }
    }
}
