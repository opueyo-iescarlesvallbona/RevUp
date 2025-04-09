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

        }

        void LoadData()
        {
            if (post != null)
            {
                f.lblTitol.Text = "Detalls del post " + post.id;
                f.txtTitle.Text = post.title;
                f.txtDescription.Text = post.description;
                f.txtAddress.Text = post.address;
                f.txtPostDate.Text = post.post_date.ToString("dd-MM-yyyy");
                f.txtMemberName.Text = r.GetAllMembers("", "", "", "").Where(x => x.id.Equals(post.member_id)).Select(x => x.membername).FirstOrDefault();
                if(post.route_id != null)
                {
                    f.txtRoute.Text = r.GetRouteById(Int32.Parse(post.route_id.ToString())).name;
                }
                f.txtPostType.Text = post.post_type1.name;

                f.dataGridViewComments.DataSource = r.GetCommentsByPostId(post.id);
                f.dataGridViewLikes.DataSource = r.GetLikesByPostId(post.id);

                f.lblNumComments.Text = "Number of comments: " + post.comments.ToString();
                f.lblNumLikes.Text = "Number of likes: " + post.likes.ToString();


                if (post.picture != null)
                {
                    Image i =  Image.FromFile(post.picture);
                    f.pictureBox.Image = i;
                }
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
