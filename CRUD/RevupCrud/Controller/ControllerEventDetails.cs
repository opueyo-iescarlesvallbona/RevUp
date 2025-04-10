using RepositoriRevUp.Model;
using RevupCrud.Model;
using RevupCrud.View;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RevupCrud.Controller
{
    public class ControllerEventDetails
    {
        RepositoriCrud r = new RepositoriCrud();
        ViewEventDetails f;
        club_event c_event;
        bool OpenedFromDetails = false;

        void SetListeners()
        {

        }

        void LoadData()
        {
            if (c_event != null)
            {
                f.lblTitol.Text = "Detalls de l'event " + c_event.id;
                f.txtName.Text = c_event.name;
                f.txtAddress.Text = c_event.address;
                f.dateTimeStartDate.Value = c_event.start_date;
                f.dateTimeEndDate.Value = c_event.end_date;
                f.txtDescription.Text = c_event.description;
                f.txtState.Text = c_event.event_state.name;
                f.txtClub.Text = c_event.club.name;

                if(c_event.route_start_date != null)
                {
                    f.dateTimeRouteStartDate.Value = c_event.route_start_date ?? DateTime.Now;
                }
                else
                {
                    f.dateTimeRouteStartDate.Checked = false;
                }

                if (c_event.picture != null)
                {
                    f.pictureBox1.Image = Image.FromFile(c_event.picture);
                }

                if (f.txtClub.Text != "")
                {
                    f.btnOpenClub.Click += BtnOpenClub_Click;
                }

                if (!OpenedFromDetails)
                {
                    f.btnGuardar.Enabled = false;
                    f.btnDelete.Enabled = true;
                    f.btnUpdate.Enabled = true;
                }
                else
                {
                    f.btnOpenClub.Visible = false;
                    f.txtClub.Width = f.txtAddress.Width;
                }
            }
            else
            {
                if (!OpenedFromDetails)
                {
                    f.btnDelete.Enabled = false;
                    f.btnUpdate.Enabled = false;
                    f.btnGuardar.Enabled = true;
                }
            }
        }

        private void BtnOpenClub_Click(object sender, EventArgs e)
        {
            if (f.txtClub.Text != "")
            {
                club club = r.GetAllClubs().Where(x => x.name.Equals(f.txtClub.Text)).FirstOrDefault();
                if (club != null)
                {
                    new ControllerClubDetails(club, new ViewClubDetails(), true);
                }
            }
        }

        public ControllerEventDetails(club_event c_event, ViewEventDetails form, bool OpenedFromDetails = false)
        {
            this.OpenedFromDetails = OpenedFromDetails;
            if (c_event != null)
            {
                this.c_event = c_event;
            }
            if (form != null)
            {
                f = form;
            }
            if (OpenedFromDetails)
            {
                f.btnDelete.Enabled = false;
                f.btnGuardar.Enabled = false;
                f.btnUpdate.Enabled = false;
            }
            SetListeners();
            LoadData();
            f.Show();
        }
    }
}
