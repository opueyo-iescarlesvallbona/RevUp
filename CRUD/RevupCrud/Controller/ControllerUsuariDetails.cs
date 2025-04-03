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
            f.txtName.TextChanged += Txt_TextChanged;
            f.txtMemberName.TextChanged += Txt_TextChanged;
            f.txtExperience.TextChanged += Txt_TextChanged;
            f.txtEmail.TextChanged += Txt_TextChanged;
            f.txtDateBirth.TextChanged += Txt_TextChanged;
            f.txtLocation.TextChanged += Txt_TextChanged;
            f.chbFounder.CheckedChanged += Chb_CheckedChanged;
        }

        private void Txt_TextChanged(object sender, EventArgs e)
        {
            (sender as TextBox).BackColor = System.Drawing.Color.White;
        }

        private void Chb_CheckedChanged(object sender, EventArgs e)
        {
            if(usuari != null)
            {
                if (f.chbFounder.Checked)
                {
                    f.dataGridViewClubs.DataSource = GetMembersClubTable(true);
                }
                else
                {
                    f.dataGridViewClubs.DataSource = GetMembersClubTable(false);
                }
            }           
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

                
                f.txtLocation.Text = r.GetLocationById(usuari.location_id).municipality;

                f.txtExperience.Text = usuari.experience.ToString();
                f.txtEmail.Text = usuari.email;
                f.txtDateBirth.Text = usuari.date_of_birth.ToString("yyyy-MM-dd");
                f.txtLoginDate.Text = usuari.login_date.Date.ToString("yyyy-MM-dd");
                f.txtDescription.Text = usuari.description;
                f.dataGridViewCars.DataSource = usuari.cars.ToList();
                f.dataGridViewPosts.DataSource = usuari.posts.ToList();
                f.dataGridViewFriends.DataSource = r.GetFriends(usuari.id);
                f.dataGridViewClubs.DataSource = GetMembersClubTable(false);


                f.btnDelete.Click += BtnDelete_Click;
                f.btnUpdate.Click += BtnUpdate_Click;
                f.btnGuardar.Click += BtnGuardar_Click_Update;

                f.btnGuardar.Enabled = false;
                f.btnDelete.Enabled = true;
                f.btnUpdate.Enabled = true;


                f.txtLocation.ReadOnly = true;
                f.txtExperience.ReadOnly = true;
                f.txtEmail.ReadOnly = true;
                f.txtDateBirth.ReadOnly = true;
                f.txtDescription.ReadOnly = true;
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

                f.txtLocation.AutoCompleteCustomSource.AddRange(r.GetAllLocations().OrderBy(x => x.municipality).Select(x => x.municipality).ToArray());
                f.txtLocation.ReadOnly = false;

                f.txtExperience.ReadOnly = false;
                f.txtEmail.ReadOnly = false;
                f.txtDateBirth.ReadOnly = false;
                f.txtDescription.ReadOnly = false;

                f.btnGuardar.Click += BtnGuardar_Click;
                f.btnDelete.Enabled = false;
                f.btnUpdate.Enabled = false;
                f.btnGuardar.Enabled = true;
            }
            f.chbFounder.Checked = false;
        }

        List<MemberClubTable> GetMembersClubTable(bool founder)
        {
            List<MemberClubTable> membersTable = new List<MemberClubTable>();
            List<member_club> clubs = new List<member_club>();
            if (founder)
            {
                clubs = r.GetMemberClubsByMemberIdIsFounder(usuari.id);
            }
            else
            {
                clubs = r.GetMemberClubsByMemberId(usuari.id);
            }            
            foreach (member_club c in clubs)
            {
                MemberClubTable member = new MemberClubTable
                {
                    Id = c.club.id,
                    name = c.club.name,
                    role = c.member_club_role.name,
                    join_date = c.join_date
                };
                membersTable.Add(member);
            }
            return membersTable;

        }

        private void BtnGuardar_Click_Update(object sender, EventArgs e)
        {
            if (Comprovacions())
            {
                usuari.location_id = r.GetAllLocations().Where(x => x.municipality.Equals(f.txtLocation.Text.ToString())).FirstOrDefault().id;
                usuari.name = f.txtName.Text;
                usuari.membername = f.txtMemberName.Text;
                usuari.gender_id = r.GetAllGenders().Where(x => x.name.Equals(f.comboGender.SelectedValue.ToString())).FirstOrDefault().id;
                usuari.experience = Convert.ToInt32(f.txtExperience.Text);
                usuari.date_of_birth = Convert.ToDateTime(f.txtDateBirth.Text);
                usuari.email = f.txtEmail.Text;
                usuari.description = f.txtDescription.Text;

                try
                {
                    r.UpdateMember(usuari);
                    LoadData();
                }
                catch
                {

                }
            }
        }

        private bool Comprovacions()
        {
            bool ok = true;
            string error = "S'han trobat els següents errors:          \n\n";
            // name comprovations
            if (f.txtName.Text.Equals(""))
            {
                f.txtName.BackColor = System.Drawing.Color.Red;
                error = error + "· El nom no pot estar buit\n";
                ok = false;
            }

            // membername comprovations
            if (f.txtMemberName.Text.Equals(""))
            {
                f.txtMemberName.BackColor = System.Drawing.Color.Red;
                error = error + "· El nom d'usuari no pot estar buit\n";
                ok = false;
            }
            else if (r.GetAllMembers("", "", "", "").Where(x => x.membername.Equals(f.txtMemberName.Text)).ToList().Count>0)
            {
                if(r.GetAllMembers("", "", "", "").Where(x => x.membername.Equals(f.txtMemberName.Text)).FirstOrDefault().id != usuari.id)
                {
                    f.txtMemberName.BackColor = System.Drawing.Color.Red;
                    error = error + "· El nom de usuari ja existeix\n";
                    ok = false;
                }
            }

            // location comprovations
            if (f.txtLocation.Text.Equals(""))
            {
                f.txtLocation.BackColor = System.Drawing.Color.Red;
                error = error + "· La població no pot estar buida\n";
                ok = false;
            }
            else if (r.GetAllLocations().Where(x => x.municipality.Equals(f.txtLocation.Text.ToString())).ToList().Count==0)
            {
                f.txtLocation.BackColor = System.Drawing.Color.Red;
                error = error + "· La població no existeix\n";
                ok = false;
            }

            // experience comprovations
            try
            {
                int experience = Convert.ToInt32(f.txtExperience.Text);
            }
            catch
            {
                f.txtExperience.BackColor = System.Drawing.Color.Red;
                error = error + "· L'experiencia ha de ser un nombre\n";
                ok = false;
            }

            // email comprovations
            if (f.txtEmail.Text.Equals(""))
            {
                f.txtEmail.BackColor = System.Drawing.Color.Red;
                error = error + "· L'email no pot estar buit\n";
                ok = false;
            }

            // date of birth comprovations
            try
            {
                DateTime date = Convert.ToDateTime(f.txtDateBirth.Text);
            }
            catch
            {
                f.txtDateBirth.BackColor = System.Drawing.Color.Red;
                error = error + "· La data de naixement no es correcta\n";
                ok = false;
            }
            if (!ok)
            {
                MessageBox.Show(error);
            }
            return ok;
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

            f.txtLocation.ReadOnly = false;
            f.txtDescription.ReadOnly = false;
            f.txtLocation.AutoCompleteCustomSource.AddRange(r.GetAllLocations().OrderBy(x => x.municipality).Select(x => x.municipality).ToArray());

            f.txtExperience.ReadOnly = false;
            f.txtEmail.ReadOnly = false;
            f.txtDateBirth.ReadOnly = false;
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Segur que vols esborrar l'usuari? S'esborraràn totes les seves dades associades", "Precaució",
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

            if (Comprovacions())
            {
                member usuari = new member
                {
                    name = f.txtName.Text,
                    membername = f.txtMemberName.Text,
                    gender_id = r.GetAllGenders().Where(x => x.name.Equals(f.comboGender.SelectedValue.ToString())).FirstOrDefault().id,
                    location_id = r.GetAllLocations().Where(x => x.municipality.Equals(f.txtLocation.Text.ToString())).FirstOrDefault().id,
                    experience = Convert.ToInt32(f.txtExperience.Text),
                    email = f.txtEmail.Text,
                    date_of_birth = Convert.ToDateTime(f.txtDateBirth.Text),
                    login_date = DateTime.Now,
                    password = "SystemPassword",
                    description = f.txtDescription.Text
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
