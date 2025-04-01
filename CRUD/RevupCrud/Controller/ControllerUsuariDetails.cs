using RepositoriRevUp.Model;
using RevupCrud.Model;
using RevupCrud.View;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace RevupCrud.Controller
{
    public class ControllerUsuariDetails
    {
        RepositoriCrud r = new RepositoriCrud();
        ViewUsuariDetails f;
        member usuari;


        void SetListeners()
        {
            if(usuari == null)
            {
                f.txtName.TextChanged += TxtName_TextChanged;
                f.txtMemberName.TextChanged += TxtName_TextChanged;
                f.txtExperience.TextChanged += TxtName_TextChanged;
                f.txtEmail.TextChanged += TxtName_TextChanged;
                f.txtDateBirth.TextChanged += TxtName_TextChanged;
            }
        }

        private void TxtName_TextChanged(object sender, EventArgs e)
        {
            (sender as TextBox).BackColor = System.Drawing.Color.White;
        }

        void LoadData()
        {
            if(usuari != null)
            {
                f.lblTitol.Text = "Detalls de l'usuari " + usuari.id;
                f.txtName.Text = usuari.name;
                f.txtMemberName.Text = usuari.membername;

                List<string> genders = new List<string>();
                genders.Add(r.GetAllGenders().Where(x => x.id.Equals(usuari.gender_id)).FirstOrDefault().name);
                f.comboGender.DataSource = genders;

                List<string> locations = new List<string>();
                locations.Add(r.GetLocationById(usuari.location_id).municipality);
                f.comboLocation.DataSource = locations;

                f.txtExperience.Text = usuari.experience.ToString();
                f.txtEmail.Text = usuari.email;
                f.txtDateBirth.Text = usuari.date_of_birth.ToString("yyyy-MM-dd");
                f.txtLoginDate.Text = usuari.login_date.Date.ToString("yyyy-MM-dd");
                f.dataGridViewCars.DataSource = usuari.cars.ToList();
                f.dataGridViewPosts.DataSource = usuari.posts.ToList();
                f.dataGridViewFriends.DataSource = r.GetFriends(usuari.id);
                f.dataGridViewClubs.DataSource = r.GetClubs(usuari.id);

                f.btnDelete.Click += BtnDelete_Click;
                f.btnUpdate.Click += BtnUpdate_Click;
                f.btnGuardar.Click += BtnGuardar_Click_Update;

                f.btnGuardar.Enabled = false;
                f.btnDelete.Enabled = true;
                f.btnUpdate.Enabled = true;


                f.comboLocation.Enabled = false;
                f.txtExperience.ReadOnly = true;
                f.txtEmail.ReadOnly = true;
                f.txtDateBirth.ReadOnly = true;
                f.comboGender.Enabled = false;
                f.txtName.ReadOnly = true;
                f.txtMemberName.ReadOnly = true;
            }
            else
            {
                f.lblTitol.Text = "Nou Usuari";
                f.txtName.ReadOnly = false;
                f.txtMemberName.ReadOnly = false;
                f.comboGender.DataSource = r.GetAllGenders().OrderBy(x => x.name).Select(x=>x.name).ToList();
                f.comboGender.Enabled = true;
                f.comboLocation.DataSource = r.GetAllLocations().OrderBy(x => x.municipality).Select(x => x.municipality).ToList();
                f.comboLocation.Enabled = true;
                f.txtExperience.ReadOnly = false;
                f.txtEmail.ReadOnly = false;
                f.txtDateBirth.ReadOnly = false;

                f.btnGuardar.Click += BtnGuardar_Click;
                f.btnDelete.Enabled = false;
                f.btnUpdate.Enabled = false;
                f.btnGuardar.Enabled = true;
            }
        }

        private void BtnGuardar_Click_Update(object sender, EventArgs e)
        {
            usuari.name = f.txtName.Text;
            usuari.membername = f.txtMemberName.Text;
            usuari.gender_id = r.GetAllGenders().Where(x => x.name.Equals(f.comboGender.SelectedValue.ToString())).FirstOrDefault().id;
            usuari.location_id = r.GetAllLocations().Where(x => x.municipality.Equals(f.comboLocation.SelectedValue.ToString())).FirstOrDefault().id;
            usuari.experience = Convert.ToInt32(f.txtExperience.Text);
            usuari.email = f.txtEmail.Text;
            usuari.date_of_birth = Convert.ToDateTime(f.txtDateBirth.Text);

            try
            {
                r.UpdateMember(usuari);
                LoadData();
            }
            catch
            {

            }
            
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            f.btnDelete.Enabled = false;
            f.btnUpdate.Enabled = false;
            f.btnGuardar.Enabled = true;

            f.txtName.ReadOnly = false;
            f.txtMemberName.ReadOnly = false;
            f.comboGender.Enabled = true;
            f.comboGender.DataSource = r.GetAllGenders().OrderBy(x => x.name).Select(x => x.name).ToList();
            f.comboGender.SelectedItem = r.GetAllGenders().Where(x => x.id.Equals(usuari.gender_id)).FirstOrDefault().name;

            f.comboLocation.Enabled = true;
            f.comboLocation.DataSource = r.GetAllLocations().OrderBy(x => x.municipality).Select(x => x.municipality).ToList();
            f.comboLocation.SelectedItem = r.GetLocationById(usuari.location_id).municipality;
            
            f.txtExperience.ReadOnly = false;
            f.txtEmail.ReadOnly = false;
            f.txtDateBirth.ReadOnly = false;
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Segur que vols esborrar l'usuari. S'esborraràn totes les seves dades associades", "Precaució",
                MessageBoxButtons.YesNo);

            if (dialogResult == DialogResult.Yes)
            {
                if (r.DeleteMember(usuari))
                {
                    f.Close();
                }
            }
            else
            {
                //DO NOTHING
            }
        }

        private void BtnGuardar_Click(object sender, EventArgs e)
        {
            bool error = false;
            if(f.txtName.Text.Equals(""))
            {
                f.txtName.BackColor = System.Drawing.Color.Red;
                error = true;
            }
            if (f.txtMemberName.Text.Equals(""))
            {
                f.txtMemberName.BackColor = System.Drawing.Color.Red;
                error = true;
            }
            if (f.txtExperience.Text.Equals(""))
            {
                f.txtExperience.BackColor = System.Drawing.Color.Red;
                error = true;
            }
            if (f.txtEmail.Text.Equals(""))
            {
                f.txtEmail.BackColor = System.Drawing.Color.Red;
                error = true;
            }
            if (f.txtDateBirth.Text.Equals(""))
            {
                f.txtDateBirth.BackColor = System.Drawing.Color.Red;
                error = true;
            }

            if (error)
            {
                MessageBox.Show("Falten camps obligatoris");
            }
            else
            {
                member usuari = new member
                {
                    name = f.txtName.Text,
                    membername = f.txtMemberName.Text,
                    gender_id = r.GetAllGenders().Where(x => x.name.Equals(f.comboGender.SelectedValue.ToString())).FirstOrDefault().id,
                    location_id = r.GetAllLocations().Where(x => x.municipality.Equals(f.comboLocation.SelectedValue.ToString())).FirstOrDefault().id,
                    experience = Convert.ToInt32(f.txtExperience.Text),
                    email = f.txtEmail.Text,
                    date_of_birth = Convert.ToDateTime(f.txtDateBirth.Text),
                    login_date = DateTime.Now,
                    password = "SystemPassword"
                };
                try
                {
                    if (r.AddMember(usuari))
                    {
                        this.usuari = usuari;
                        LoadData();
                    }
                }
                catch
                {
                }
            }
        }

        public ControllerUsuariDetails(member usuari, ViewUsuariDetails form)
        {
            if(usuari != null)
            {
                this.usuari = usuari;
            }
            if(form != null)
            {
                f = form;
            }
            SetListeners();
            LoadData();
            f.Show();
        }
    }
}
