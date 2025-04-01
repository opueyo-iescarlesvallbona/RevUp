using RepositoriRevUp;
using RepositoriRevUp.Model;
using RevupCrud.Model;
using RevupCrud.View;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace RevupCrud.Controller
{
    public class ControllerCrud
    {
        Form1 f = new Form1();
        Form Viewpassword = new Form();
        RepositoriCrud r;
        ViewUsuaris usuaris = new ViewUsuaris { TopLevel=false, TopMost=false };


        void SetListeners()
        {
            f.btnUsuaris.Click += BtnUsuaris_Click;
            f.btnClub.Click += BtnClub_Click;
        }

        private void SetListenersClub()
        {

        }

        private void BtnClub_Click(object sender, EventArgs e)
        {
            SetListenersClub();
        }

        void SetListenersUsuaris()
        {
            usuaris.btnBuscar.Click += BtnBuscar_Click;
            usuaris.dataGridView.CellFormatting += DataGridView_CellFormatting;
            usuaris.dataGridView.CellDoubleClick += DataGridView_CellDoubleClick;
            usuaris.btnInsert.Click += BtnInsert_Click;
        }

        private void BtnInsert_Click(object sender, EventArgs e)
        {
            ViewUsuariDetails f = new ViewUsuariDetails();
            f.FormClosed += UserDetailsFormClosed;
            new ControllerUsuariDetails(null, f);
        }

        private void DataGridView_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            DataGridViewColumn column = usuaris.dataGridView.Columns[e.ColumnIndex];
            if (e.RowIndex >= 0)
            {
                if (column.HeaderText.Equals("gender"))
                {
                    member m = usuaris.dataGridView.Rows[e.RowIndex].DataBoundItem as member;
                    if (m != null)
                    {
                        e.Value = m.gender.name;
                    }
                }
                else if (column.HeaderText.Equals("member_location"))
                {
                    member m = usuaris.dataGridView.Rows[e.RowIndex].DataBoundItem as member;
                    if (m != null)
                    {
                        e.Value = m.member_location.municipality;
                    }
                }
            }
        }

        private void BtnBuscar_Click(object sender, EventArgs e)
        {
            usuaris.dataGridView.DataSource = r.GetAllMembers(usuaris.txtName.Text, (usuaris.comboGender.SelectedValue as gender).name.ToString(), usuaris.txtYear.Text, (usuaris.comboLocation.SelectedValue as member_location).municipality.ToString());
        }

        private void DataGridView_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            member m = usuaris.dataGridView.Rows[e.RowIndex].DataBoundItem as member;

            member usuari = new member
            {
                id = m.id,
                name = m.name,
                gender_id = m.gender_id,
                location_id = m.location_id,
                membername = m.membername,
                experience = m.experience,
                email = m.email,
                date_of_birth = m.date_of_birth,
                login_date = m.login_date,
                cars = m.cars,
                clubs = m.clubs,
                posts = m.posts,
                routes = m.routes
            };
            ViewUsuariDetails f = new ViewUsuariDetails();
            new ControllerUsuariDetails(usuari, f);
            f.FormClosed += UserDetailsFormClosed;
        }

        void UserDetailsFormClosed(object sender, EventArgs e)
        {
            usuaris.dataGridView.DataSource = r.GetAllMembers(usuaris.txtName.Text, (usuaris.comboGender.SelectedValue as gender).name.ToString(), usuaris.txtYear.Text, (usuaris.comboLocation.SelectedValue as member_location).municipality.ToString());
        }

        private void BtnUsuaris_Click(object sender, EventArgs e)
        {
            SetListenersUsuaris();
            
            List<gender> genders = new List<gender>();
            genders.Add(new gender { name="Tots", id=-1});
            genders.AddRange(r.GetAllGenders());
            usuaris.comboGender.DataSource = genders;
            usuaris.comboGender.DisplayMember = "name";


            List<member_location> locations = new List<member_location>();
            locations.Add(new member_location { municipality = "Tots", id = -1 });
            locations.AddRange(r.GetAllLocations());
            usuaris.comboLocation.DataSource = locations;
            usuaris.comboLocation.DisplayMember = "municipality";

            usuaris.dataGridView.DataSource = r.GetAllMembers("", "", "", "");

            usuaris.dataGridView.Columns["gender_id"].Visible = false;
            usuaris.dataGridView.Columns["location_id"].Visible = false;
            usuaris.dataGridView.Columns["description"].Visible = false;
            usuaris.dataGridView.Columns["profile_picture"].Visible = false;
            usuaris.dataGridView.Columns["password"].Visible = false;
            usuaris.dataGridView.Columns["member_club"].Visible = false;
            usuaris.dataGridView.Columns["member_relation"].Visible = false;
            usuaris.dataGridView.Columns["member_relation1"].Visible = false;
            usuaris.dataGridView.Columns["messages"].Visible = false;
            usuaris.dataGridView.Columns["messages1"].Visible = false;
            usuaris.dataGridView.Columns["posts"].Visible = false;
            usuaris.dataGridView.Columns["routes"].Visible = false;
            usuaris.dataGridView.Columns["clubs"].Visible = false;
            usuaris.dataGridView.Columns["cars"].Visible = false;
            usuaris.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            usuaris.dataGridView.BorderStyle = BorderStyle.None;

            usuaris.FormBorderStyle = FormBorderStyle.None;
            f.panel.Controls.Add(usuaris);
            usuaris.Show();

        }

        void AskPassword()
        {
            Label lbl = new Label();
            lbl.Name = "lblPassword";
            TextBox txt = new TextBox();
            txt.Name = "txtPassword";
            Button btn = new Button();
            btn.Name = "btnPassword";

            Viewpassword.Text = "Introduir Contrasenya";
            Viewpassword.Size = new System.Drawing.Size(320, 150);
            Viewpassword.StartPosition = FormStartPosition.CenterScreen;

            lbl.Text = "Introdueix la contrasenya";
            lbl.Location = new System.Drawing.Point(10, 20);
            lbl.AutoSize = true;

            txt.Location = new System.Drawing.Point(10, 50);
            txt.Width = 200;
            txt.PasswordChar = '*';

            btn.Text = "Acceptar";
            btn.Location = new System.Drawing.Point(220, 48);
            btn.Click += Btn_ClickPassword;

            Viewpassword.Controls.Add(lbl);
            Viewpassword.Controls.Add(txt);
            Viewpassword.Controls.Add(btn);
            
            Viewpassword.ShowDialog();
        }

        private void Btn_ClickPassword(object sender, EventArgs e)
        {
            string password = (Viewpassword.Controls.Find("txtPassword", true).FirstOrDefault() as TextBox).Text;
            
            if (TryConnectToDatabase(password))
            {
                Viewpassword.Dispose();
                f.ShowDialog();
            }
            else
            {
                MessageBox.Show("Error al connectar amb la base de dades.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        bool TryConnectToDatabase(string password)
        {
            try
            {
                string connectionString = ConfigurationManager.ConnectionStrings["revupEntities"].ConnectionString;

                connectionString = connectionString.Replace("{PASSWORD_PLACEHOLDER}", password);



                revupEntities db = new revupEntities(password);
                db.Database.Connection.Open();
                Repositori.password = password;
                r = new RepositoriCrud();
                return true;
            }
            catch
            {
                return false;
            }
        }

        public ControllerCrud()
        {
            SetListeners();
            AskPassword();
        }
    }
}
