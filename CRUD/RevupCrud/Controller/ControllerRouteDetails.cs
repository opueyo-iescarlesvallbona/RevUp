using RepositoriRevUp.Model;
using RevupCrud.Model;
using RevupCrud.View;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RevupCrud.Controller
{
    public class ControllerRouteDetails
    {
        RepositoriCrud r = new RepositoriCrud();
        ViewRouteDetails f;
        route route;

        void SetListeners()
        {

        }

        void LoadData()
        {
            if (route != null)
            {
                f.lblTitol.Text = "Route details - " + route.id;
                if (route.datetime != null)
                {
                    f.txtDate.Text = route.datetime?.ToString("dd-MM-yyyy") ?? "";
                }
                
                f.txtDistance.Text = route.distance.ToString();
                f.txtDuration.Text = route.duration.ToString();
                f.txtDescription.Text = route.description;
                f.txtElevationGain.Text = route.elevation_gain.ToString();
                if (route.terrain_type != null)
                {
                    f.txtTerrainType.Text = route.terrain_type.name;
                }
                f.txtStartAddress.Text = route.start_address;
                f.txtEndAddress.Text = route.end_address;
                f.txtMaxElevation.Text = route.max_elevation.ToString();
                f.txtName.Text = route.name;
                f.txtMemberName.Text = r.GetAllMembers("", "", "", "").Where(x => x.id.Equals(route.member_id)).Select(x => x.membername).FirstOrDefault();


                if (f.txtMemberName.Text != "")
                {
                    f.btnOpenMember.Click += BtnOpenMember_Click;
                }
            }
        }

        private void BtnOpenMember_Click(object sender, EventArgs e)
        {
            member member = r.GetAllMembers("", "", "", "").Where(x => x.membername.Equals(f.txtMemberName.Text)).FirstOrDefault();
            if (member != null)
            {
                new ControllerUsuariDetails(member, new ViewUsuariDetails(), true);
            }
        }

        public ControllerRouteDetails(route route, ViewRouteDetails form)
        {
            if (route != null)
            {
                this.route = route;
            }
            if (form != null)
            {
                f = form;
            }
            SetListeners();
            LoadData();
            f.Show();
        }
    }
}
