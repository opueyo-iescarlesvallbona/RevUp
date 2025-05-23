namespace RevupCrud
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.btnUsuaris = new System.Windows.Forms.Button();
            this.btn_comments = new System.Windows.Forms.Button();
            this.btn_post = new System.Windows.Forms.Button();
            this.btnClub = new System.Windows.Forms.Button();
            this.panel = new System.Windows.Forms.Panel();
            this.btnEvents = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnUsuaris
            // 
            this.btnUsuaris.Font = new System.Drawing.Font("Microsoft Sans Serif", 18F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnUsuaris.Location = new System.Drawing.Point(46, 90);
            this.btnUsuaris.Name = "btnUsuaris";
            this.btnUsuaris.Size = new System.Drawing.Size(177, 81);
            this.btnUsuaris.TabIndex = 0;
            this.btnUsuaris.Text = "Members";
            this.btnUsuaris.UseVisualStyleBackColor = true;
            // 
            // btn_comments
            // 
            this.btn_comments.Font = new System.Drawing.Font("Microsoft Sans Serif", 18F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btn_comments.Location = new System.Drawing.Point(46, 464);
            this.btn_comments.Name = "btn_comments";
            this.btn_comments.Size = new System.Drawing.Size(177, 81);
            this.btn_comments.TabIndex = 1;
            this.btn_comments.Text = "Comments";
            this.btn_comments.UseVisualStyleBackColor = true;
            // 
            // btn_post
            // 
            this.btn_post.Font = new System.Drawing.Font("Microsoft Sans Serif", 18F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btn_post.Location = new System.Drawing.Point(46, 370);
            this.btn_post.Name = "btn_post";
            this.btn_post.Size = new System.Drawing.Size(177, 81);
            this.btn_post.TabIndex = 2;
            this.btn_post.Text = "Posts";
            this.btn_post.UseVisualStyleBackColor = true;
            // 
            // btnClub
            // 
            this.btnClub.Font = new System.Drawing.Font("Microsoft Sans Serif", 18F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnClub.Location = new System.Drawing.Point(46, 183);
            this.btnClub.Name = "btnClub";
            this.btnClub.Size = new System.Drawing.Size(177, 81);
            this.btnClub.TabIndex = 3;
            this.btnClub.Text = "Clubs";
            this.btnClub.UseVisualStyleBackColor = true;
            // 
            // panel
            // 
            this.panel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.panel.Location = new System.Drawing.Point(284, 48);
            this.panel.Name = "panel";
            this.panel.Padding = new System.Windows.Forms.Padding(2);
            this.panel.Size = new System.Drawing.Size(1118, 549);
            this.panel.TabIndex = 5;
            // 
            // btnEvents
            // 
            this.btnEvents.Font = new System.Drawing.Font("Microsoft Sans Serif", 18F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnEvents.Location = new System.Drawing.Point(46, 276);
            this.btnEvents.Name = "btnEvents";
            this.btnEvents.Size = new System.Drawing.Size(177, 81);
            this.btnEvents.TabIndex = 6;
            this.btnEvents.Text = "Events";
            this.btnEvents.UseVisualStyleBackColor = true;
            this.btnEvents.Click += new System.EventHandler(this.button1_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1436, 651);
            this.Controls.Add(this.btnEvents);
            this.Controls.Add(this.panel);
            this.Controls.Add(this.btnClub);
            this.Controls.Add(this.btn_post);
            this.Controls.Add(this.btn_comments);
            this.Controls.Add(this.btnUsuaris);
            this.Name = "Form1";
            this.Text = "RevUp";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);

        }

        #endregion

        public System.Windows.Forms.Panel panel;
        public System.Windows.Forms.Button btnUsuaris;
        public System.Windows.Forms.Button btn_comments;
        public System.Windows.Forms.Button btn_post;
        public System.Windows.Forms.Button btnClub;
        public System.Windows.Forms.Button btnEvents;
    }
}

