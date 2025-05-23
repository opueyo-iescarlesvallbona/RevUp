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
    public class ControllerPostDetails
    {
        RepositoriCrud r = new RepositoriCrud();
        ViewPostDetails f;
        post post;
        bool OpenedFromDetails = false;

        void SetListeners()
        {
            if (!OpenedFromDetails)
            {
                f.dataGridViewComments.CellDoubleClick += DataGridViewComments_DoubleClick;
                f.dataGridViewLikes.CellDoubleClick += DataGridViewLikes_DoubleClick;
            }            
        }

        private void DataGridViewLikes_DoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                member m = f.dataGridViewLikes.Rows[e.RowIndex].DataBoundItem as member;
                ViewUsuariDetails form = new ViewUsuariDetails();
                new ControllerUsuariDetails(m, form, true);
            }
        }

        private void DataGridViewComments_DoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            if(e.RowIndex >= 0)
            {
                post_comment comment = f.dataGridViewComments.Rows[e.RowIndex].DataBoundItem as post_comment;
                ViewCommentDetails form = new ViewCommentDetails();
                new ControllerCommentsDetails(comment, form, true);
            }
        }

        void LoadData()
        {
            if (post != null)
            {
                f.lblTitol.Text = "Post details - " + post.id;
                f.txtTitle.Text = post.title;
                f.txtDescription.Text = post.description;
                if(post.member_location != null)
                {
                    f.txtAddress.Text = post.member_location.municipality;
                }
                f.txtPostDate.Text = post.post_date.ToString("dd-MM-yyyy");
                f.txtMemberName.Text = r.GetAllMembers("", "", "", "").Where(x => x.id.Equals(post.member_id)).Select(x => x.membername).FirstOrDefault();
                if(post.route_id != null)
                {
                    f.txtRoute.Text = r.GetRouteById(Int32.Parse(post.route_id.ToString())).name;
                }
                f.txtPostType.Text = post.post_type1.name;

                f.dataGridViewComments.DataSource = r.GetCommentsByPostId(post.id);
                f.dataGridViewComments.Columns["member"].Visible = false;
                f.dataGridViewComments.Columns["post"].Visible = false;
                f.dataGridViewLikes.DataSource = r.GetLikesByPostId(post.id);

                f.lblNumComments.Text = "Number of comments: " + post.comments.ToString();
                f.lblNumLikes.Text = "Number of likes: " + post.likes.ToString();


                
                f.btnDelete.Click += BtnDelete_Click;
                f.btnOpenMember.Click += BtnOpenMember_Click;

                f.btnOpenRoute.Click += BtnOpenRoute_Click;
                if (!OpenedFromDetails)
                {
                    f.btnDelete.Enabled = true;
                }
                    

                f.txtTitle.Enabled = false;
                f.txtDescription.Enabled = false;
                f.txtAddress.Enabled = false;
                f.txtPostDate.Enabled = false;
                f.txtMemberName.Enabled = false;
                f.txtRoute.Enabled = false;
                f.txtPostType.Enabled = false;
            }
        }

        private void BtnOpenRoute_Click(object sender, EventArgs e)
        {
            if (f.txtRoute.Text != "")
            {
                route route = r.GetRouteById(post.route.id);
                if (route != null)
                {
                    new ControllerRouteDetails(route, new ViewRouteDetails());
                }
            }
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Segur que vols esborrar el post? S'esborraràn totes les seves dades associades", "Precaució",
                MessageBoxButtons.YesNo);

            if (dialogResult == DialogResult.Yes)
            {
                if (r.DeletePost(post))
                {
                    f.Close();
                }
            }
            else
            {
                //DO NOTHING
            }
        }

        private void BtnOpenMember_Click(object sender, EventArgs e)
        {
            if(f.txtMemberName.Text != "")
            {
                member member = r.GetAllMembers("", "", "", "").Where(x => x.membername.Equals(f.txtMemberName.Text)).FirstOrDefault();
                if (member != null)
                {
                    new ControllerUsuariDetails(member, new ViewUsuariDetails(), true);
                }
            }
        }

        private void Txt_TextChanged(object sender, EventArgs e)
        {
            (sender as TextBox).BackColor = System.Drawing.Color.White;
        }
        public ControllerPostDetails(post post, ViewPostDetails form, bool OpenedFromDetails=false)
        {
            this.OpenedFromDetails = OpenedFromDetails;
            if (post != null)
            {
                this.post = post;
            }
            if (form != null)
            {
                f = form;
            }
            if (OpenedFromDetails)
            {
                f.btnDelete.Enabled = false;
            }
            SetListeners();
            LoadData();
            f.Show();
        }
    }
}
