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
        ViewClubs clubs = new ViewClubs { TopLevel = false, TopMost = false };
        ViewPosts posts = new ViewPosts { TopLevel = false, TopMost = false };
        ViewComments comments = new ViewComments { TopLevel = false, TopMost = false };


        void SetListeners()
        {
            f.btnUsuaris.Click += BtnUsuaris_Click;
            f.btnClub.Click += BtnClub_Click;
            f.btn_post.Click += BtnPost_Click;
            f.btn_comments.Click += BtnComment_Click;            
        }

        #region comments

        private void SetListenersComment()
        {
            comments.btnBuscar.Click += BtnBuscar_ClickComment;
            comments.dataGridView.CellDoubleClick += DataGridView_CellDoubleClickComment;
            comments.dataGridView.CellFormatting += DataGridView_CellFormattingComment;
        }

        private void DataGridView_CellDoubleClickComment(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                post_comment comment = comments.dataGridView.Rows[e.RowIndex].DataBoundItem as post_comment;
                
                ViewCommentDetails f = new ViewCommentDetails();                
                new ControllerCommentsDetails(comment, f);
                f.FormClosed += CommentDetailsFormClosed;
            }
        }

        private void BtnBuscar_ClickComment(object sender, EventArgs e)
        {
            List<post_comment> Listcomments = SearchComments();
            if (Listcomments != null)
            {
                comments.dataGridView.DataSource = Listcomments;                
            }
            else
            {
                comments.dataGridView.DataSource = new List<post_comment>();               
            }
        }

        private void CommentDetailsFormClosed(object sender, FormClosedEventArgs e)
        {
            List<post_comment> Listcomments = SearchComments();
            if (Listcomments != null)
            {
                comments.dataGridView.DataSource = Listcomments;                
            }            
        }

        List<post_comment> SearchComments()
        {
            if (comments.dateTimeTo.Value.Date < comments.dateTimeFrom.Value.Date && comments.dateTimeTo.Checked && comments.dateTimeFrom.Checked)
            {
                MessageBox.Show("The To date cannot be earlier than the From date", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
            List<post_comment> Listcomments = r.GetAllComments().OrderBy(x=>x.datetime).ToList();            
            if (String.IsNullOrEmpty(comments.txtPost.Text) && String.IsNullOrEmpty(comments.txtMember.Text))
            {
                MessageBox.Show("Post id or member id is required", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
            if (comments.txtPost.Text != "" || comments.txtMember.Text != "")
            {
                if(comments.txtPost.Text != "" && comments.txtMember.Text != "")
                {
                    try
                    {
                        Listcomments = Listcomments.Where(x => x.post_id == int.Parse(comments.txtPost.Text) && x.member_id == int.Parse(comments.txtMember.Text)).ToList();
                    }
                    catch
                    {
                        MessageBox.Show("Post id and member id have to be an integer.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        return null;
                    }
                }
                else if(comments.txtPost.Text != "")
                {
                    try
                    {
                        Listcomments = Listcomments.Where(x => x.post_id == int.Parse(comments.txtPost.Text)).ToList();
                    }
                    catch
                    {
                        MessageBox.Show("Post id has to be an integer.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        return null;
                    }
                }
                else if(comments.txtMember.Text != "")
                {
                    try
                    {
                        Listcomments = Listcomments.Where(x => x.member_id == int.Parse(comments.txtMember.Text)).ToList();
                    }
                    catch
                    {
                        MessageBox.Show("Member id has to be an integer.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        return null;
                    }
                }
            }
            else
            {
                MessageBox.Show("Post id or member id are required.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }          
            if (comments.dateTimeFrom.Checked && comments.dateTimeTo.Checked)
            {
                Listcomments = Listcomments.Where(x => x.datetime.Date >= comments.dateTimeFrom.Value.Date && x.datetime.Date <= comments.dateTimeTo.Value.Date).ToList();
            }
            else if (comments.dateTimeTo.Checked)
            {
                Listcomments = Listcomments.Where(x => x.datetime.Date <= comments.dateTimeTo.Value.Date).ToList();
            }
            else if (comments.dateTimeFrom.Checked)
            {
                Listcomments = Listcomments.Where(x => x.datetime.Date >= comments.dateTimeFrom.Value.Date).ToList();
            }
            
            return Listcomments;
        }

        private void BtnComment_Click(object sender, EventArgs e)
        {
            SetListenersComment();   
            comments.dataGridView.DataSource = new List<post_comment>();
            comments.dataGridView.Columns["member_id"].HeaderText = "member";
            comments.dataGridView.Columns["post"].Visible = false;
            comments.dataGridView.Columns["member"].Visible = false;
            FormatHeadersDataGrid(comments.dataGridView);


            comments.dateTimeFrom.Checked = false;
            comments.dateTimeTo.Checked = false;                        

            comments.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            comments.dataGridView.BorderStyle = BorderStyle.None;

            comments.FormBorderStyle = FormBorderStyle.None;
            f.panel.Controls.Clear();
            f.panel.Controls.Add(comments);
            comments.Show();
        }

        void DataGridView_CellFormattingComment(object sender, DataGridViewCellFormattingEventArgs e)
        {
            DataGridViewColumn column = comments.dataGridView.Columns[e.ColumnIndex];
            if (e.RowIndex >= 0)
            {
                if (column.HeaderText.Equals("Member"))
                {
                    post_comment p = comments.dataGridView.Rows[e.RowIndex].DataBoundItem as post_comment;
                    if (p != null)
                    {
                        e.Value = p.member.name;
                    }
                }                
            }
        }

        #endregion

        #region post
        private void SetListnersPost()
        {
            posts.btnBuscar.Click += BtnBuscar_ClickPost;
            posts.dataGridView.CellFormatting += DataGridView_CellFormattingPost;
            posts.dataGridView.CellDoubleClick += DataGridView_CellDoubleClickPost;
        }

        void DataGridView_CellDoubleClickPost(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                post p = posts.dataGridView.Rows[e.RowIndex].DataBoundItem as post;
                post post = new post
                {
                    id = p.id,
                    title = p.title,
                    description = p.description,
                    picture = p.picture,
                    likes = p.likes,
                    comments = p.comments,
                    post_date = p.post_date,
                    member = p.member,
                    post_type1 = p.post_type1,
                    route = p.route,
                    member_id = p.member_id,
                    route_id = p.route_id
                };
                ViewPostDetails f = new ViewPostDetails();
                new ControllerPostDetails(post, f);
                f.FormClosed += PostDetailsFormClosed;
            }
        }

        private void PostDetailsFormClosed(object sender, FormClosedEventArgs e)
        {
            List<post> Listposts = SearchPosts();
            if (Listposts != null)
            {
                posts.dataGridView.DataSource = Listposts;
            }
        }

        void DataGridView_CellFormattingPost(object sender, DataGridViewCellFormattingEventArgs e)
        {
            DataGridViewColumn column = posts.dataGridView.Columns[e.ColumnIndex];
            if (e.RowIndex >= 0)
            {
                if (column.HeaderText.Equals("Post type"))
                {
                    post p = posts.dataGridView.Rows[e.RowIndex].DataBoundItem as post;
                    if (p != null)
                    {
                        e.Value = p.post_type1.name;
                    }
                }
                else if (column.HeaderText.Equals("Member"))
                {
                    post p = posts.dataGridView.Rows[e.RowIndex].DataBoundItem as post;
                    if (p != null)
                    {
                        e.Value = p.member.membername;
                    }
                }
                else if(column.HeaderText.Equals("Route"))
                {
                    post p = posts.dataGridView.Rows[e.RowIndex].DataBoundItem as post;
                    if (p.route != null)
                    {
                        e.Value = p.route.name;
                    }
                }
            }
        }

        void BtnBuscar_ClickPost(object sender, EventArgs e)
        {
            List<post> Listposts = SearchPosts();
            if (Listposts != null)
            {
                posts.dataGridView.DataSource = Listposts;
            }
        }

        List<post> SearchPosts()
        {
            if (posts.dateTimeTo.Value.Date < posts.dateTimeFrom.Value.Date && posts.dateTimeTo.Checked && posts.dateTimeFrom.Checked)
            {
                MessageBox.Show("The To date cannot be earlier than the From date", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
            List<post> Listposts = r.GetAllPosts();
            if (posts.txtTitle.Text != "")
            {
                Listposts = Listposts.Where(x => x.title.Contains(posts.txtTitle.Text)).ToList();
            }
            if ((posts.comboPostType.SelectedItem as post_type).name != "Tots")
            {
                Listposts = Listposts.Where(x => x.post_type.Equals((posts.comboPostType.SelectedValue as post_type).id)).ToList();
            }
            if (posts.dateTimeFrom.Checked && posts.dateTimeTo.Checked)
            {
                Listposts = Listposts.Where(x => x.post_date.Date >= posts.dateTimeFrom.Value.Date && x.post_date.Date <= posts.dateTimeTo.Value.Date).ToList();
            }
            else if (posts.dateTimeTo.Checked)
            {
                Listposts = Listposts.Where(x => x.post_date.Date <= posts.dateTimeTo.Value.Date).ToList();
            }
            else if (posts.dateTimeFrom.Checked)
            {
                Listposts = Listposts.Where(x => x.post_date.Date >= posts.dateTimeFrom.Value.Date).ToList();
            }

            switch (posts.comboOrder.SelectedValue)
            {
                case "More to less likes":
                    Listposts = Listposts.OrderByDescending(x => x.likes).ToList();
                    break;
                case "Less to more likes":
                    Listposts = Listposts.OrderBy(x => x.likes).ToList();
                    break;
                case "More to less comments":
                    Listposts = Listposts.OrderByDescending(x => x.comments).ToList();
                    break;
                case "Less to more comments":
                    Listposts = Listposts.OrderBy(x => x.comments).ToList();
                    break;
            }

            switch ((posts.comboPostType.SelectedItem as post_type).name)
            {
                case "Image":
                    posts.dataGridView.Columns["picture"].Visible = true;
                    posts.dataGridView.Columns["route"].Visible = false;
                    break;
                case "Route":
                    posts.dataGridView.Columns["picture"].Visible = false;
                    posts.dataGridView.Columns["route"].Visible = true;
                    break;
                case "Text":
                    posts.dataGridView.Columns["picture"].Visible = false;
                    posts.dataGridView.Columns["route"].Visible = false;
                    break;
                default:
                    posts.dataGridView.Columns["picture"].Visible = true;
                    posts.dataGridView.Columns["route"].Visible = true;
                    break;
            }
            return Listposts;
        }

        private void BtnPost_Click(object sender, EventArgs e)
        {
            SetListnersPost();
            List<post> Listposts = r.GetAllPosts();
            
            List<post_type> post_Types = new List<post_type>();
            post_Types.Add(new post_type { name = "Tots", id = -1 });
            post_Types.AddRange(r.GetAllPostTypes());
            posts.comboPostType.DataSource = post_Types.ToList();

            posts.comboPostType.DisplayMember = "name";

            List<string> orders = new List<string>();
            orders.Add("More to less likes");
            orders.Add("Less to more likes");
            orders.Add("More to less comments");
            orders.Add("Less to more comments");
            posts.comboOrder.DataSource = orders;

            posts.dateTimeFrom.Checked = false;
            posts.dateTimeTo.Checked = false;
            posts.dataGridView.DataSource = Listposts.OrderByDescending(x => x.likes).ToList();
            posts.dataGridView.Columns["post_type"].Visible = false;
            posts.dataGridView.Columns["post_comment"].Visible = false;
            posts.dataGridView.Columns["route_id"].Visible = false;
            posts.dataGridView.Columns["post_type1"].HeaderText = "post type";
            posts.dataGridView.Columns["members"].Visible = false;
            FormatHeadersDataGrid(posts.dataGridView);

            posts.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            posts.dataGridView.BorderStyle = BorderStyle.None;

            posts.FormBorderStyle = FormBorderStyle.None;
            f.panel.Controls.Clear();
            f.panel.Controls.Add(posts);
            posts.Show();
        }

        #endregion

        #region club
        private void SetListenersClub()
        {
            clubs.btnBuscar.Click += BtnBuscar_ClickClubs;
            clubs.dataGridView.CellFormatting += DataGridView_CellFormattingClub;
            clubs.dataGridView.CellDoubleClick += DataGridView_CellDoubleClickClub;
            clubs.btnInsert.Click += BtnInsert_ClickClub;
        }

        private void BtnInsert_ClickClub(object sender, EventArgs e)
        {
            ViewClubDetails f = new ViewClubDetails();
            f.FormClosed += ClubDetailsFormClosed;
            new ControllerClubDetails(null, f, true);
        }

        private void DataGridView_CellDoubleClickClub(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                club c = clubs.dataGridView.Rows[e.RowIndex].DataBoundItem as club;

                club club = new club
                {
                    id = c.id,
                    name = c.name,
                    description = c.description,
                    picture = c.picture,
                    founder = c.founder,
                    member = c.member,
                    member_club = c.member_club,
                    club_event = c.club_event
                };

                ViewClubDetails f = new ViewClubDetails();
                new ControllerClubDetails(club, f, false);
                f.FormClosed += ClubDetailsFormClosed;
            }
        }

        private void ClubDetailsFormClosed(object sender, FormClosedEventArgs e)
        {
            if (clubs.txtFounder.Text.Equals(""))
            {
                clubs.dataGridView.DataSource = r.GetAllClubs()
                    .Where(x => x.name.Contains(clubs.txtName.Text)).ToList();
            }
            else
            {
                clubs.dataGridView.DataSource = r.GetAllClubs()
                    .Where(x => x.name.Contains(clubs.txtName.Text))
                    .Where(x => x.member.membername.Equals(clubs.txtFounder.Text)).ToList();
            }
                
            List<club> Listclubs = r.GetAllClubs();

            clubs.txtName.AutoCompleteCustomSource.AddRange(Listclubs.Select(x => x.name).ToArray());

            List<member> founders = r.GetMembersById(Listclubs.Select(x => x.founder).ToList());
            clubs.txtFounder.AutoCompleteCustomSource.AddRange(founders.Select(x => x.membername).ToArray());
        }

        private void DataGridView_CellFormattingClub(object sender, DataGridViewCellFormattingEventArgs e)
        {
            DataGridViewColumn column = clubs.dataGridView.Columns[e.ColumnIndex];
            if (e.RowIndex >= 0)
            {
                if (column.HeaderText.Equals("Founder"))
                {
                    club c = clubs.dataGridView.Rows[e.RowIndex].DataBoundItem as club;
                    if (c != null)
                    {
                        e.Value = c.member.membername;
                    }
                }
            }
        }

        private void BtnBuscar_ClickClubs(object sender, EventArgs e)
        {
            if (clubs.txtFounder.Text.Equals(""))
            {
                clubs.dataGridView.DataSource = r.GetAllClubs()
                    .Where(x => x.name.Contains(clubs.txtName.Text)).ToList();
            }
            else
            {
                clubs.dataGridView.DataSource = r.GetAllClubs()
                    .Where(x => x.name.Contains(clubs.txtName.Text))
                    .Where(x => x.member.membername.Equals(clubs.txtFounder.Text)).ToList();
            }
        }

        private void BtnClub_Click(object sender, EventArgs e)
        {
            SetListenersClub();
            List<club> Listclubs = r.GetAllClubs();

            clubs.txtName.AutoCompleteCustomSource.AddRange(Listclubs.Select(x => x.name).ToArray());

            List<member> founders = r.GetMembersById(Listclubs.Select(x => x.founder).ToList());
            clubs.txtFounder.AutoCompleteCustomSource.AddRange(founders.Select(x=>x.membername).ToArray());

            clubs.dataGridView.DataSource = Listclubs;

            clubs.dataGridView.Columns["picture"].Visible = false;
            clubs.dataGridView.Columns["founder"].Visible = false;
            clubs.dataGridView.Columns["member_club"].Visible = false;
            clubs.dataGridView.Columns["club_event"].Visible = false;
            clubs.dataGridView.Columns["member"].HeaderText = "Founder";
            clubs.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            clubs.dataGridView.BorderStyle = BorderStyle.None;

            FormatHeadersDataGrid(clubs.dataGridView);

            clubs.FormBorderStyle = FormBorderStyle.None;
            f.panel.Controls.Clear();
            f.panel.Controls.Add(clubs);
            clubs.Show();
        }

        #endregion

        #region usuaris
        void SetListenersUsuaris()
        {
            usuaris.btnBuscar.Click += BtnBuscar_Click;
            usuaris.dataGridView.CellFormatting += DataGridView_CellFormattingUsuari;
            usuaris.dataGridView.CellDoubleClick += DataGridView_CellDoubleClickUsuari;
            usuaris.btnInsert.Click += BtnInsert_ClickUsuari;
        }

        private void BtnInsert_ClickUsuari(object sender, EventArgs e)
        {
            ViewUsuariDetails f = new ViewUsuariDetails();
            f.FormClosed += UserDetailsFormClosed;
            new ControllerUsuariDetails(null, f);
        }

        private void DataGridView_CellFormattingUsuari(object sender, DataGridViewCellFormattingEventArgs e)
        {
            DataGridViewColumn column = usuaris.dataGridView.Columns[e.ColumnIndex];
            if (e.RowIndex >= 0)
            {
                if (column.HeaderText.Equals("Gender"))
                {
                    member m = usuaris.dataGridView.Rows[e.RowIndex].DataBoundItem as member;
                    if (m != null)
                    {
                        e.Value = m.gender.name;
                    }
                }
                else if (column.HeaderText.Equals("Member_location"))
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

        private void DataGridView_CellDoubleClickUsuari(object sender, DataGridViewCellEventArgs e)
        {
            if(e.RowIndex >= 0)
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
                    description = m.description,
                    date_of_birth = m.date_of_birth,
                    login_date = m.login_date,                    
                    clubs = m.clubs,
                    posts = m.posts,
                    routes = m.routes
                };
                ViewUsuariDetails f = new ViewUsuariDetails();
                new ControllerUsuariDetails(usuari, f);
                f.FormClosed += UserDetailsFormClosed;
            }       
        }

        void UserDetailsFormClosed(object sender, EventArgs e)
        {
            usuaris.dataGridView.DataSource = r.GetAllMembers(usuaris.txtName.Text, (usuaris.comboGender.SelectedValue as gender).name.ToString(), usuaris.txtYear.Text, (usuaris.comboLocation.SelectedValue as member_location).municipality.ToString());
            usuaris.txtName.AutoCompleteCustomSource.AddRange(r.GetAllMembers("", "", "", "").Select(x => x.name).ToArray());
            usuaris.txtName.AutoCompleteCustomSource.AddRange(r.GetAllMembers("", "", "", "").Where(x => x.membername != null).Select(x => x.membername).ToArray());
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


            usuaris.txtName.AutoCompleteCustomSource.AddRange(r.GetAllMembers("", "", "", "").Select(x => x.name).ToArray());
            usuaris.txtName.AutoCompleteCustomSource.AddRange(r.GetAllMembers("", "", "", "").Where(x=>x.membername!=null).Select(x => x.membername).ToArray());

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
            usuaris.dataGridView.Columns["posts1"].Visible = false;
            usuaris.dataGridView.Columns["post_comment"].Visible = false;
            usuaris.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            usuaris.dataGridView.BorderStyle = BorderStyle.None;

            FormatHeadersDataGrid(usuaris.dataGridView);

            usuaris.FormBorderStyle = FormBorderStyle.None;
            f.panel.Controls.Clear();
            f.panel.Controls.Add(usuaris);
            usuaris.Show();

        }

        #endregion

        #region password
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
            
            Viewpassword.Show();
            btn.PerformClick();
        }

        private void Btn_ClickPassword(object sender, EventArgs e)
        {
            string password = (Viewpassword.Controls.Find("txtPassword", true).FirstOrDefault() as TextBox).Text;
            password = "RevUpFounders26";
            if (TryConnectToDatabase(password))
            {
                Viewpassword.Dispose();
                r.GetAllPostTypes();
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
        #endregion


        public void FormatHeadersDataGrid(DataGridView tbl)
        {
            for (int i = 0; i < tbl.Columns.Count; i++)
            {
                var header = tbl.Columns[i].HeaderText;
                tbl.Columns[i].HeaderText = char.ToUpper(header[0]) + header.Substring(1);
            }
        }

        public ControllerCrud()
        {
            SetListeners();
            AskPassword();
        }
    }
}
