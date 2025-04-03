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
    public class ControllerClubDetails
    {
        RepositoriCrud r = new RepositoriCrud();
        ViewClubDetails f;
        club club;

        void SetListeners()
        {
            f.txtName.TextChanged += Txt_TextChanged;
            f.txtFounder.TextChanged += Txt_TextChanged;
        }

        private void Txt_TextChanged(object sender, EventArgs e)
        {
            (sender as TextBox).BackColor = System.Drawing.Color.White;
        }

        void LoadData()
        {
            if (club != null)
            {
                f.lblTitol.Text = "Detalls del club " + club.id;
                f.txtName.Text = club.name;
                f.txtFounder.Text = club.member.membername;
                f.txtCreationDate.Text = club.creation_date.Date.ToString("yyyy-MM-dd");
                if(club.description != null)
                {
                    f.txtDescription.Text = club.description;
                }

                f.dataGridViewEvents.DataSource = r.GetEventsByClub(club.id);
                f.dataGridViewMembers.DataSource = GetMembersClubTable();
                f.dataGridViewMembers.Columns["Id"].Visible = false;
                f.dataGridViewMembers.Columns["name"].HeaderText = "MemberName";


                f.btnDelete.Click += BtnDelete_Click;
                f.btnUpdate.Click += BtnUpdate_Click;
                f.btnGuardar.Click += BtnGuardar_Click_Update;

                f.btnGuardar.Enabled = false;
                f.btnDelete.Enabled = true;
                f.btnUpdate.Enabled = true;

                f.txtName.ReadOnly = true;
                f.txtFounder.ReadOnly = true;
                f.txtDescription.ReadOnly = true;
            }
            else
            {
                f.lblTitol.Text = "Nou club";
                f.txtName.ReadOnly = false;
                f.txtFounder.ReadOnly = false;
                f.txtFounder.AutoCompleteCustomSource.AddRange(r.GetAllMembers("","","","").OrderBy(x => x.name).Select(x => x.membername).ToArray());
                f.txtDescription.ReadOnly = false;

                f.btnGuardar.Click += BtnGuardar_Click;
                f.btnDelete.Enabled = false;
                f.btnUpdate.Enabled = false;
                f.btnGuardar.Enabled = true;
            }
        }

        List<MemberClubTable> GetMembersClubTable()
        {
            List<MemberClubTable> membersTable = new List<MemberClubTable>();
            List<member_club> members = r.GetMemberClubsByClubId(club.id);
            foreach (member_club m in members)
            {
                MemberClubTable member = new MemberClubTable
                {
                    Id = m.member.id,
                    name = m.member.membername,
                    role = m.member_club_role.name,
                    join_date = m.join_date
                };
                membersTable.Add(member);
            }
            return membersTable;

        }

        private void BtnGuardar_Click_Update(object sender, EventArgs e)
        {
            if (Comprovacions())
            {
                club.name = f.txtName.Text;
                club.founder = r.GetAllMembers("", "", "", "").Where(x => x.membername.Equals(f.txtFounder.Text)).FirstOrDefault().id;
                club.description = f.txtDescription.Text;

                try
                {
                    r.UpdateClub(club);
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
                error = error + "· El nom del club no pot estar buit\n";
                ok = false;
            }
            else if (r.GetAllClubs().Where(x => x.name.Equals(f.txtName.Text)).ToList().Count > 0)
            {
                if (r.GetAllClubs().Where(x => x.name.Equals(f.txtName.Text)).FirstOrDefault().id != club.id)
                {
                    f.txtName.BackColor = System.Drawing.Color.Red;
                    error = error + "· El nom de club ja existeix\n";
                    ok = false;
                }
            }

            // founder comprovations
            if (f.txtFounder.Text.Equals(""))
            {
                f.txtFounder.BackColor = System.Drawing.Color.Red;
                error = error + "· El fundador no pot estar buit\n";
                ok = false;
            }
            else if (r.GetAllMembers("","","","").Where(x => x.membername.Equals(f.txtFounder.Text.ToString())).ToList().Count == 0)
            {
                f.txtFounder.BackColor = System.Drawing.Color.Red;
                error = error + "· L'usuari no existeix\n";
                ok = false;
            }

            if (!ok)
            {
                MessageBox.Show(error);
            }
            return ok;
        }

        private void BtnGuardar_Click(object sender, EventArgs e)
        {

            if (Comprovacions())
            {
                club club = new club
                {
                    name = f.txtName.Text,
                    founder = r.GetAllMembers("","","","").Where(x=>x.membername.Equals(f.txtFounder.Text)).FirstOrDefault().id,
                    creation_date = DateTime.Now,
                    description = f.txtDescription.Text
                };
                try
                {
                    if (r.AddClub(club))
                    {
                        this.club = club;
                        LoadData();
                    }
                }
                catch
                {
                }

            }
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            f.btnDelete.Enabled = false;
            f.btnUpdate.Enabled = false;
            f.btnGuardar.Enabled = true;

            f.txtName.ReadOnly = false;
            f.txtFounder.ReadOnly = false;
            f.txtFounder.AutoCompleteCustomSource.AddRange(r.GetAllMembers("", "", "", "").OrderBy(x => x.name).Select(x => x.membername).ToArray());
            f.txtDescription.ReadOnly = false;
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Segur que vols esborrar el club? S'esborraràn totes les seves dades associades", "Precaució",
                MessageBoxButtons.YesNo);

            if (dialogResult == DialogResult.Yes)
            {
                if (r.DeleteClub(club))
                {
                    f.Close();
                }
            }
            else
            {
                //DO NOTHING
            }
        }
        public ControllerClubDetails(club club, ViewClubDetails form)
        {
            if (club != null)
            {
                this.club = club;
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
