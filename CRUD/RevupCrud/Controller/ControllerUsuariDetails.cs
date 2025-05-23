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
using System.Xml.Linq;

namespace RevupCrud.Controller
{
    public class ControllerUsuariDetails
    {
        RepositoriCrud r = new RepositoriCrud();
        ViewUsuariDetails f = new ViewUsuariDetails();
        member usuari;
        bool OpenedFromDetails = false;


        void SetListeners()
        {
            f.txtName.TextChanged += Txt_TextChanged;
            f.txtMemberName.TextChanged += Txt_TextChanged;
            f.txtExperience.TextChanged += Txt_TextChanged;
            f.txtEmail.TextChanged += Txt_TextChanged;
            f.txtLocation.TextChanged += Txt_TextChanged;
            f.chbFounder.CheckedChanged += Chb_CheckedChanged;
            if (!OpenedFromDetails)
            {
                f.dataGridViewCars.CellDoubleClick += DataGridViewCars_CellDoubleClick;
                f.dataGridViewClubs.CellDoubleClick += DataGridViewClubs_CellDoubleClick;
                f.dataGridViewPosts.CellDoubleClick += DataGridViewPosts_CellDoubleClick;
                f.dataGridViewRelations.CellDoubleClick += DataGridViewRelations_CellDoubleClick;
            }            
        }

        private void DataGridViewRelations_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                List<int> list = new List<int> { (f.dataGridViewRelations.Rows[e.RowIndex].DataBoundItem as MemberRelationTable).Id };
                List<member> m = r.GetMembersById(list);
                ViewUsuariDetails form = new ViewUsuariDetails();
                new ControllerUsuariDetails(m[0], form, true);                
            }
        }

        private void DataGridViewPosts_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                post p = f.dataGridViewPosts.Rows[e.RowIndex].DataBoundItem as post;
                ViewPostDetails form = new ViewPostDetails();
                new ControllerPostDetails(p, form, true);                
            }
        }

        private void DataGridViewClubs_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                club clubT = r.GetClubById((f.dataGridViewClubs.Rows[e.RowIndex].DataBoundItem as MemberClubTable).Id);
                ViewClubDetails form = new ViewClubDetails();
                new ControllerClubDetails(clubT, form, true);
                form.FormClosed += ClubDetailsFormClosed;
            }
        }

        private void ClubDetailsFormClosed(object sender, FormClosedEventArgs e)
        {
            f.dataGridViewClubs.DataSource = GetMembersClubTable(false);
        }

        private void DataGridViewCars_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                car carT = r.GetCarById((f.dataGridViewCars.Rows[e.RowIndex].DataBoundItem as MemberCarTable).Id);
                ViewCarDetails form = new ViewCarDetails();
                new ControllerCarDetails(carT, form);
                form.FormClosed += CarDetailsFormClosed;
            }
        }

        private void CarDetailsFormClosed(object sender, FormClosedEventArgs e)
        {
            f.dataGridViewCars.DataSource = GetMemberCarTable(usuari.id);
        }

        private void Txt_TextChanged(object sender, EventArgs e)
        {
            (sender as TextBox).BackColor = System.Drawing.Color.White;
        }

        private void Chb_CheckedChanged(object sender, EventArgs e)
        {
            if (usuari != null)
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
            if (usuari != null)
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
                
                f.dtpDateBirth.Value = usuari.date_of_birth;
                f.txtLoginDate.Text = usuari.login_date.Date.ToString("dd-MM-yyyy");
                f.txtDescription.Text = usuari.description;
                f.dataGridViewCars.DataSource = GetMemberCarTable(usuari.id);
                f.dataGridViewPosts.DataSource = usuari.posts.ToList();
                f.dataGridViewRelations.DataSource = GetMemberRelationTable(usuari.id);
                f.dataGridViewClubs.DataSource = GetMembersClubTable(false);

                

                f.btnDelete.Click += BtnDelete_Click;
                f.btnUpdate.Click += BtnUpdate_Click;
                f.btnGuardar.Click += BtnGuardar_Click_Update;

                if (!OpenedFromDetails)
                {
                    f.btnGuardar.Enabled = false;
                    f.btnDelete.Enabled = true;
                    f.btnUpdate.Enabled = true;
                }
                    


                f.txtLocation.Enabled = false;
                f.txtMemberName.ReadOnly = true;
                f.txtExperience.Enabled = false;
                f.txtEmail.Enabled = false;
                f.dtpDateBirth.Enabled = false;
                f.txtDescription.Enabled = false;
                f.comboGender.Enabled = false;
                f.txtName.Enabled = false;
                f.txtMemberName.Enabled = false;
            }
            else
            {
                f.lblTitol.Text = "Nou Usuari";
                f.txtName.Enabled = true;
                f.txtMemberName.Enabled = true;
                f.comboGender.DataSource = r.GetAllGenders().OrderBy(x => x.name).Select(x => x.name).ToList();
                f.comboGender.Enabled = true;
                f.dtpDateBirth.Value = DateTime.Now;

                f.txtLocation.AutoCompleteCustomSource.AddRange(r.GetAllLocations().OrderBy(x => x.municipality).Select(x => x.municipality).ToArray());
                f.txtLocation.Enabled = true;

                f.txtExperience.Enabled = true;
                f.txtEmail.Enabled = true;
                f.dtpDateBirth.Enabled = true;
                f.txtDescription.Enabled = true;

                f.btnGuardar.Click += BtnGuardar_Click;
                if (!OpenedFromDetails)
                {
                    f.btnDelete.Enabled = false;
                    f.btnUpdate.Enabled = false;
                    f.btnGuardar.Enabled = true;
                }
                
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

        List<MemberCarTable> GetMemberCarTable(int id)
        {
            List<MemberCarTable> carsTable = new List<MemberCarTable>();
            List<car> cars = r.GetMemberCars(id);
            foreach (var car in cars)
            {
                MemberCarTable carT = new MemberCarTable
                {
                    Id = car.id,
                    Brand = car.model.brand.name,
                    Model = car.model.model_name,
                    Year = car.model_year,
                    HP = car.horse_power
                };
                carsTable.Add(carT);
            }
            return carsTable;
        }

        List<MemberRelationTable> GetMemberRelationTable(int id)
        {
            List<MemberRelationTable> friendsTable = new List<MemberRelationTable>();
            List<member_relation> friends = r.GetRelations(id);
            foreach (var friend in friends)
            {
                MemberRelationTable friendT = new MemberRelationTable
                {
                    State = friend.relation_state.name
                };
                if(friend.member_id1 == id)
                {
                    friendT.Id = friend.member_id2;
                    friendT.Friend = friend.member1.name;
                }
                else
                {
                    friendT.Id = friend.member_id1;
                    friendT.Friend = friend.member.name;
                }
                friendsTable.Add(friendT);
            }
            return friendsTable;
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
                usuari.date_of_birth = f.dtpDateBirth.Value;
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
            string error = "Errors found:          \n\n";
            // name comprovations
            if (f.txtName.Text.Equals(""))
            {
                f.txtName.BackColor = System.Drawing.Color.Red;
                error = error + "· The name can't be empty.\n";
                ok = false;
            }

            // membername comprovations
            if (f.txtMemberName.Text.Equals(""))
            {
                f.txtMemberName.BackColor = System.Drawing.Color.Red;
                error = error + "· The username can't be empty.\n";
                ok = false;
            }
            else if (r.GetAllMembers("", "", "", "").Where(x => x.membername.Equals(f.txtMemberName.Text)).ToList().Count > 0)
            {
                if (r.GetAllMembers("", "", "", "").Where(x => x.membername.Equals(f.txtMemberName.Text)).FirstOrDefault().id != usuari.id)
                {
                    f.txtMemberName.BackColor = System.Drawing.Color.Red;
                    error = error + "· The username already exists.\n";
                    ok = false;
                }
            }

            // location comprovations
            if (f.txtLocation.Text.Equals(""))
            {
                f.txtLocation.BackColor = System.Drawing.Color.Red;
                error = error + "· The municipality can't be empty.\n";
                ok = false;
            }
            else if (r.GetAllLocations().Where(x => x.municipality.Equals(f.txtLocation.Text.ToString())).ToList().Count == 0)
            {
                f.txtLocation.BackColor = System.Drawing.Color.Red;
                error = error + "· The municipality doesn't exist.\n";
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
                error = error + "· The experience can't be a number.\n";
                ok = false;
            }

            // email comprovations
            if (f.txtEmail.Text.Equals(""))
            {
                f.txtEmail.BackColor = System.Drawing.Color.Red;
                error = error + "· The mail can't be empty.\n";
                ok = false;
            }

            // date of birth comprovations
            if (f.dtpDateBirth.Value > DateTime.Now)
            {
                error = error + "· The date of birth can't be older than now.\n";
                ok = false;
            }
            else if (f.dtpDateBirth.Value.AddYears(16) > DateTime.Now)
            {
                error = error + "· The minimum age is: 18 years.\n";
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

            f.txtName.Enabled = true;
            f.comboGender.Enabled = true;
            f.comboGender.DataSource = r.GetAllGenders().OrderBy(x => x.name).Select(x => x.name).ToList();
            f.comboGender.SelectedItem = r.GetAllGenders().Where(x => x.id.Equals(usuari.gender_id)).FirstOrDefault().name;

            f.txtLocation.Enabled = true;
            f.txtDescription.Enabled = true;
            f.txtLocation.AutoCompleteCustomSource.AddRange(r.GetAllLocations().OrderBy(x => x.municipality).Select(x => x.municipality).ToArray());

            f.txtExperience.Enabled = true;
            f.txtEmail.Enabled = true;
            f.dtpDateBirth.Enabled = true;
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
                    date_of_birth = f.dtpDateBirth.Value,
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

        public ControllerUsuariDetails(member usuari, ViewUsuariDetails form, bool OpenedFromDetails=false)
        {
            this.OpenedFromDetails = OpenedFromDetails;
            if (usuari != null)
            {
                this.usuari = usuari;
            }
            if (form != null)
            {
                f = form;
            }
            if (OpenedFromDetails)
            {
                f.btnDelete.Enabled = false;
                f.btnUpdate.Enabled = false;
                f.btnGuardar.Enabled = false;
            }
            SetListeners();
            LoadData();
            f.Show();
        }
    }
}
