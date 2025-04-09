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
    public class ControllerCommentsDetails
    {
        RepositoriCrud r = new RepositoriCrud();
        ViewCommentDetails f;
        post_comment comment;

        void SetListeners()
        {
            f.btnDelete.Click += BtnDelete_Click;
            f.btnPost.Click += BtnPost_Click;
            f.btnMember.Click += BtnMember_Click;
        }

        private void BtnMember_Click(object sender, EventArgs e)
        {
            ViewUsuariDetails form = new ViewUsuariDetails();
            form.btnDelete.Enabled = false;
            form.btnGuardar.Enabled = false;
            form.btnUpdate.Enabled = false;
            new ControllerUsuariDetails(comment.member, form, true);            
        }

        private void BtnPost_Click(object sender, EventArgs e)
        {
            ViewPostDetails form = new ViewPostDetails();
            form.btnDelete.Enabled = false;
            new ControllerPostDetails(comment.post, form, true);
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            r.DeleteComment(comment);
            f.Close();
        }

        void LoadData()
        {
            if(comment != null)
            {
                f.lblTitol.Text = "Comment - " + comment.id.ToString();                
                f.txtDate.Text = comment.datetime.ToString();
                f.txtPost.Text = comment.post.title;
                f.txtMember.Text = comment.member.name;
                f.txtContent.Text = comment.comment_content;
            }
        }

        public ControllerCommentsDetails(post_comment comment, ViewCommentDetails form)
        {
            if (comment != null)
            {
                this.comment = comment;
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
