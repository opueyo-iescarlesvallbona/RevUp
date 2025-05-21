using RepositoriRevUp.Model;
using RevupCrud.Model;
using RevupCrud.View;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

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
            f.btnDelete.Click += BtnDelete_Click;
            f.btnUpdate.Click += BtnUpdate_Click;

            f.txtName.TextChanged += Txt_TextChanged;
            f.txtAddress.TextChanged += Txt_TextChanged;            
            f.txtClub.TextChanged += Txt_TextChanged;
            f.comboState.TextChanged += Cb_TextChanged;
            f.dateTimeStartDate.TextChanged += Dtp_TextChanged;
            f.dateTimeEndDate.TextChanged += Dtp_TextChanged;
            f.dateTimeRouteStartDate.TextChanged += Dtp_TextChanged;
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            f.btnDelete.Enabled = false;
            f.btnUpdate.Enabled = false;
            f.btnGuardar.Enabled = true;

            f.txtName.Enabled = true;
            f.txtClub.Enabled = true;
            f.txtAddress.Enabled = true;
            f.txtDescription.Enabled = true;
            f.dateTimeStartDate.Enabled = true;
            f.dateTimeEndDate.Enabled = true;
            f.dateTimeRouteStartDate.Enabled = true;
            f.comboState.Enabled = true;

            f.comboState.DataSource = r.GetAllEventStates().OrderBy(x => x.name).Select(x => x.name).ToList();


            f.btnOpenClub.Enabled = false;
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Are you sure you want to delete the event? All associated data will be deleted", "Caution",
                MessageBoxButtons.YesNo);

            if (dialogResult == DialogResult.Yes)
            {
                if (r.DeleteEvent(c_event))
                {
                    f.Close();
                }
            }
            else
            {
                //DO NOTHING
            }
        }

        void LoadData()
        {
            if (c_event != null)
            {
                f.txtName.Enabled = false;
                f.txtClub.Enabled = false;
                f.txtAddress.Enabled = false;
                f.txtDescription.Enabled = false;
                f.dateTimeStartDate.Enabled = false;
                f.dateTimeEndDate.Enabled = false;
                f.dateTimeRouteStartDate.Enabled = false;
                f.comboState.Enabled = false;


                f.lblTitol.Text = "Detalls de l'event " + c_event.id;
                f.txtName.Text = c_event.name;
                f.txtAddress.Text = c_event.address;
                f.dateTimeStartDate.Value = c_event.start_date;
                f.dateTimeEndDate.Value = c_event.end_date;
                f.txtDescription.Text = c_event.description;
                f.comboState.DataSource = r.GetAllEventStates().OrderBy(x => x.name).Select(x => x.name).ToList();
                f.comboState.SelectedText = c_event.event_state.name;
                f.txtClub.Text = c_event.club.name;

                if (c_event.route_start_date != null)
                {
                    f.dateTimeRouteStartDate.Value = c_event.route_start_date ?? DateTime.Now;
                }
                else
                {
                    f.dateTimeRouteStartDate.Checked = false;
                }

                if (c_event.picture != null)
                {
                    try
                    {
                        f.pictureBox1.Image = Image.FromFile(c_event.picture);
                    }
                    catch
                    {

                    }
                    
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

                f.btnGuardar.Click += BtnGuardar_Click_Update;
            }
            else
            {
                f.lblTitol.Text = "New Event";
                f.txtName.Enabled = true;
                f.txtClub.Enabled = true;
                f.txtAddress.Enabled = true;
                f.txtDescription.Enabled = true;
                f.dateTimeStartDate.Enabled = true;
                f.dateTimeEndDate.Enabled = true;
                f.dateTimeRouteStartDate.Enabled = true;
                f.comboState.Enabled = true;

                f.comboState.DataSource = r.GetAllEventStates().OrderBy(x => x.name).Select(x => x.name).ToList();

                f.dateTimeEndDate.Value = DateTime.Now;
                f.dateTimeStartDate.Value = DateTime.Now;
                f.dateTimeRouteStartDate.Value = DateTime.Now;
                f.btnOpenClub.Enabled = false;

                f.btnGuardar.Click += BtnGuardar_Click;

                if (!OpenedFromDetails)
                {
                    f.btnDelete.Enabled = false;
                    f.btnUpdate.Enabled = false;
                    f.btnGuardar.Enabled = true;
                }
            }
        }

        private void BtnGuardar_Click_Update(object sender, EventArgs e)
        {
            if (Comprovacions())
            {
                c_event.name = f.txtName.Text;
                c_event.address = f.txtAddress.Text;
                c_event.start_date = f.dateTimeStartDate.Value;
                c_event.end_date = f.dateTimeEndDate.Value;
                if(f.dateTimeRouteStartDate.Checked)
                {
                    c_event.route_start_date = f.dateTimeRouteStartDate.Value;
                }
                else
                {
                    c_event.route_start_date = null;
                }
                c_event.description = f.txtDescription.Text;
                c_event.club_id = r.GetAllClubs().Where(x => x.name.Equals(f.txtClub.Text)).FirstOrDefault().id;
                c_event.state = r.GetAllEventStates().Where(x => x.name.Equals(f.comboState.SelectedValue.ToString())).FirstOrDefault().id;

                try
                {
                    r.UpdateEvent(c_event);
                    LoadData();
                }
                catch
                {

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

        private void BtnGuardar_Click(object sender, EventArgs e)
        {
            DateTime? ruta;
            if (f.dateTimeRouteStartDate.Checked)
            {
                ruta = f.dateTimeRouteStartDate.Value;
            }
            else
            {
                ruta = null;
            }

            if (Comprovacions())
            {
                club_event club_Event = new club_event
                {
                    name = f.txtName.Text,
                    address = f.txtAddress.Text,
                    start_date = f.dateTimeStartDate.Value,
                    route_start_date = ruta,
                    end_date = f.dateTimeEndDate.Value,
                    description = f.txtDescription.Text,
                    club_id = r.GetAllClubs().Where(x => x.name.Equals(f.txtClub.Text)).FirstOrDefault().id,
                    state = r.GetAllEventStates().Where(x => x.name.Equals(f.comboState.SelectedValue.ToString())).FirstOrDefault().id
                };
                try
                {
                    if (r.AddEvent(club_Event))
                    {
                        this.c_event = club_Event;
                        LoadData();
                    }
                }
                catch
                {
                }
            }
        }

        private void Txt_TextChanged(object sender, EventArgs e)
        {
            (sender as TextBox).BackColor = System.Drawing.Color.White;
        }

        private void Cb_TextChanged(object sender, EventArgs e)
        {
            (sender as ComboBox).BackColor = System.Drawing.Color.White;
        }

        private void Dtp_TextChanged(object sender, EventArgs e)
        {
            (sender as DateTimePicker).BackColor = System.Drawing.Color.White;
        }

        private bool Comprovacions()
        {
            bool ok = true;
            string error = "Errors found:          \n\n";
            // name comprovations
            if (f.txtName.Text.Equals(""))
            {
                f.txtName.BackColor = System.Drawing.Color.Red;
                error = error + "· The name can't be empty.\n";
                ok = false;
            }

            if(f.txtAddress.Text.Equals(""))
            {
                f.txtAddress.BackColor = System.Drawing.Color.Red;
                error = error + "· The address can't be empty.\n";
                ok = false;
            }

            if (r.GetAllClubs().Where(x => x.name.Equals(f.txtClub.Text.ToString())).ToList().Count == 0)
            {
                f.txtClub.BackColor = System.Drawing.Color.Red;
                error = error + "· The club doesn't exist.\n";
                ok = false;
            }

            if (f.dateTimeStartDate.Value < DateTime.Today)
            {
                f.dateTimeStartDate.BackColor = System.Drawing.Color.Red;
                error = error + "· The start date can't be before today.\n";
                ok = false;
            }
            if(f.dateTimeStartDate.Value > f.dateTimeEndDate.Value)
            {
                f.dateTimeStartDate.BackColor = System.Drawing.Color.Red;
                error = error + "· The start date can't be after the end date.\n";
                ok = false;
            }
            if(f.dateTimeEndDate.Value < DateTime.Today)
            {
                f.dateTimeEndDate.BackColor = System.Drawing.Color.Red;
                error = error + "· The end date can't be before today.\n";
                ok = false;
            }
            if (f.dateTimeRouteStartDate.Value < f.dateTimeStartDate.Value)
            {
                f.dateTimeRouteStartDate.BackColor = System.Drawing.Color.Red;
                error = error + "· The route start date can't be before the start date.\n";
                ok = false;
            }
            if (f.dateTimeRouteStartDate.Value > f.dateTimeEndDate.Value)
            {
                f.dateTimeRouteStartDate.BackColor = System.Drawing.Color.Red;
                error = error + "· The route start date can't be after the end date.\n";
                ok = false;
            }
            if(f.dateTimeRouteStartDate.Value < DateTime.Today)
            {
                f.dateTimeRouteStartDate.BackColor = System.Drawing.Color.Red;
                error = error + "· The route start date can't be before today.\n";
                ok = false;
            }

            if (!r.GetAllEventStates().Any(x => x.name.Equals(f.comboState.SelectedValue)))
            {
                f.comboState.BackColor = System.Drawing.Color.Red;
                error = error + "· The event state does not exist.\n";
                ok = false;
            }


            if (!ok)
            {
                MessageBox.Show(error);
            }
            return ok;
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
