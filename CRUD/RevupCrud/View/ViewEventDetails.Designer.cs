﻿namespace RevupCrud.View
{
    partial class ViewEventDetails
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
            this.txtDescription = new System.Windows.Forms.TextBox();
            this.lblDescription = new System.Windows.Forms.Label();
            this.txtClub = new System.Windows.Forms.TextBox();
            this.txtName = new System.Windows.Forms.TextBox();
            this.lblEndDate = new System.Windows.Forms.Label();
            this.lblAddress = new System.Windows.Forms.Label();
            this.lblRouteStartDate = new System.Windows.Forms.Label();
            this.lblClub = new System.Windows.Forms.Label();
            this.lblStartDate = new System.Windows.Forms.Label();
            this.lblName = new System.Windows.Forms.Label();
            this.lblTitol = new System.Windows.Forms.Label();
            this.lblState = new System.Windows.Forms.Label();
            this.txtAddress = new System.Windows.Forms.TextBox();
            this.dateTimeStartDate = new System.Windows.Forms.DateTimePicker();
            this.dateTimeRouteStartDate = new System.Windows.Forms.DateTimePicker();
            this.dateTimeEndDate = new System.Windows.Forms.DateTimePicker();
            this.btnOpenClub = new System.Windows.Forms.Button();
            this.btnUpdate = new System.Windows.Forms.Button();
            this.btnDelete = new System.Windows.Forms.Button();
            this.btnGuardar = new System.Windows.Forms.Button();
            this.comboState = new System.Windows.Forms.ComboBox();
            this.SuspendLayout();
            // 
            // txtDescription
            // 
            this.txtDescription.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtDescription.Location = new System.Drawing.Point(101, 167);
            this.txtDescription.Multiline = true;
            this.txtDescription.Name = "txtDescription";
            this.txtDescription.Size = new System.Drawing.Size(438, 74);
            this.txtDescription.TabIndex = 65;
            // 
            // lblDescription
            // 
            this.lblDescription.AutoSize = true;
            this.lblDescription.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblDescription.Location = new System.Drawing.Point(12, 169);
            this.lblDescription.Name = "lblDescription";
            this.lblDescription.Size = new System.Drawing.Size(83, 17);
            this.lblDescription.TabIndex = 64;
            this.lblDescription.Text = "Description:";
            // 
            // txtClub
            // 
            this.txtClub.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtClub.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.Suggest;
            this.txtClub.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.CustomSource;
            this.txtClub.Location = new System.Drawing.Point(101, 133);
            this.txtClub.Name = "txtClub";
            this.txtClub.Size = new System.Drawing.Size(357, 20);
            this.txtClub.TabIndex = 51;
            // 
            // txtName
            // 
            this.txtName.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtName.Location = new System.Drawing.Point(101, 70);
            this.txtName.Name = "txtName";
            this.txtName.Size = new System.Drawing.Size(438, 20);
            this.txtName.TabIndex = 50;
            // 
            // lblEndDate
            // 
            this.lblEndDate.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.lblEndDate.AutoSize = true;
            this.lblEndDate.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblEndDate.Location = new System.Drawing.Point(578, 133);
            this.lblEndDate.Name = "lblEndDate";
            this.lblEndDate.Size = new System.Drawing.Size(71, 17);
            this.lblEndDate.TabIndex = 48;
            this.lblEndDate.Text = "End Date:";
            // 
            // lblAddress
            // 
            this.lblAddress.AutoSize = true;
            this.lblAddress.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblAddress.Location = new System.Drawing.Point(12, 102);
            this.lblAddress.Name = "lblAddress";
            this.lblAddress.Size = new System.Drawing.Size(64, 17);
            this.lblAddress.TabIndex = 43;
            this.lblAddress.Text = "Address:";
            // 
            // lblRouteStartDate
            // 
            this.lblRouteStartDate.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.lblRouteStartDate.AutoSize = true;
            this.lblRouteStartDate.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblRouteStartDate.Location = new System.Drawing.Point(578, 102);
            this.lblRouteStartDate.Name = "lblRouteStartDate";
            this.lblRouteStartDate.Size = new System.Drawing.Size(118, 17);
            this.lblRouteStartDate.TabIndex = 42;
            this.lblRouteStartDate.Text = "Route Start Date:";
            // 
            // lblClub
            // 
            this.lblClub.AutoSize = true;
            this.lblClub.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblClub.Location = new System.Drawing.Point(12, 132);
            this.lblClub.Name = "lblClub";
            this.lblClub.Size = new System.Drawing.Size(40, 17);
            this.lblClub.TabIndex = 40;
            this.lblClub.Text = "Club:";
            // 
            // lblStartDate
            // 
            this.lblStartDate.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.lblStartDate.AutoSize = true;
            this.lblStartDate.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblStartDate.Location = new System.Drawing.Point(578, 71);
            this.lblStartDate.Name = "lblStartDate";
            this.lblStartDate.Size = new System.Drawing.Size(76, 17);
            this.lblStartDate.TabIndex = 39;
            this.lblStartDate.Text = "Start Date:";
            // 
            // lblName
            // 
            this.lblName.AutoSize = true;
            this.lblName.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblName.Location = new System.Drawing.Point(12, 71);
            this.lblName.Name = "lblName";
            this.lblName.Size = new System.Drawing.Size(49, 17);
            this.lblName.TabIndex = 38;
            this.lblName.Text = "Name:";
            // 
            // lblTitol
            // 
            this.lblTitol.AutoSize = true;
            this.lblTitol.Font = new System.Drawing.Font("Microsoft Sans Serif", 20F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblTitol.Location = new System.Drawing.Point(12, 16);
            this.lblTitol.Name = "lblTitol";
            this.lblTitol.Size = new System.Drawing.Size(223, 31);
            this.lblTitol.TabIndex = 37;
            this.lblTitol.Text = "Detalls del event:";
            // 
            // lblState
            // 
            this.lblState.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.lblState.AutoSize = true;
            this.lblState.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblState.Location = new System.Drawing.Point(578, 166);
            this.lblState.Name = "lblState";
            this.lblState.Size = new System.Drawing.Size(45, 17);
            this.lblState.TabIndex = 66;
            this.lblState.Text = "State:";
            // 
            // txtAddress
            // 
            this.txtAddress.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtAddress.Location = new System.Drawing.Point(101, 100);
            this.txtAddress.Name = "txtAddress";
            this.txtAddress.Size = new System.Drawing.Size(438, 20);
            this.txtAddress.TabIndex = 67;
            // 
            // dateTimeStartDate
            // 
            this.dateTimeStartDate.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.dateTimeStartDate.CustomFormat = "dd / MM / yyyy";
            this.dateTimeStartDate.Format = System.Windows.Forms.DateTimePickerFormat.Custom;
            this.dateTimeStartDate.Location = new System.Drawing.Point(702, 70);
            this.dateTimeStartDate.Name = "dateTimeStartDate";
            this.dateTimeStartDate.Size = new System.Drawing.Size(145, 20);
            this.dateTimeStartDate.TabIndex = 70;
            // 
            // dateTimeRouteStartDate
            // 
            this.dateTimeRouteStartDate.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.dateTimeRouteStartDate.CustomFormat = "dd / MM / yyyy";
            this.dateTimeRouteStartDate.Format = System.Windows.Forms.DateTimePickerFormat.Custom;
            this.dateTimeRouteStartDate.Location = new System.Drawing.Point(702, 102);
            this.dateTimeRouteStartDate.Name = "dateTimeRouteStartDate";
            this.dateTimeRouteStartDate.RightToLeft = System.Windows.Forms.RightToLeft.No;
            this.dateTimeRouteStartDate.ShowCheckBox = true;
            this.dateTimeRouteStartDate.Size = new System.Drawing.Size(145, 20);
            this.dateTimeRouteStartDate.TabIndex = 71;
            // 
            // dateTimeEndDate
            // 
            this.dateTimeEndDate.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.dateTimeEndDate.CustomFormat = "dd / MM / yyyy";
            this.dateTimeEndDate.Format = System.Windows.Forms.DateTimePickerFormat.Custom;
            this.dateTimeEndDate.Location = new System.Drawing.Point(702, 133);
            this.dateTimeEndDate.Name = "dateTimeEndDate";
            this.dateTimeEndDate.Size = new System.Drawing.Size(145, 20);
            this.dateTimeEndDate.TabIndex = 73;
            // 
            // btnOpenClub
            // 
            this.btnOpenClub.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnOpenClub.Location = new System.Drawing.Point(464, 132);
            this.btnOpenClub.Name = "btnOpenClub";
            this.btnOpenClub.Size = new System.Drawing.Size(75, 23);
            this.btnOpenClub.TabIndex = 74;
            this.btnOpenClub.Text = "Open Club";
            this.btnOpenClub.UseVisualStyleBackColor = true;
            // 
            // btnUpdate
            // 
            this.btnUpdate.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnUpdate.Location = new System.Drawing.Point(692, 12);
            this.btnUpdate.Name = "btnUpdate";
            this.btnUpdate.Size = new System.Drawing.Size(71, 39);
            this.btnUpdate.TabIndex = 77;
            this.btnUpdate.Text = "Update";
            this.btnUpdate.UseVisualStyleBackColor = true;
            // 
            // btnDelete
            // 
            this.btnDelete.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnDelete.Location = new System.Drawing.Point(613, 12);
            this.btnDelete.Name = "btnDelete";
            this.btnDelete.Size = new System.Drawing.Size(71, 39);
            this.btnDelete.TabIndex = 76;
            this.btnDelete.Text = "Delete";
            this.btnDelete.UseVisualStyleBackColor = true;
            // 
            // btnGuardar
            // 
            this.btnGuardar.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnGuardar.Location = new System.Drawing.Point(772, 12);
            this.btnGuardar.Name = "btnGuardar";
            this.btnGuardar.Size = new System.Drawing.Size(71, 39);
            this.btnGuardar.TabIndex = 75;
            this.btnGuardar.Text = "Save";
            this.btnGuardar.UseVisualStyleBackColor = true;
            // 
            // comboState
            // 
            this.comboState.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.comboState.FormattingEnabled = true;
            this.comboState.Location = new System.Drawing.Point(702, 165);
            this.comboState.Name = "comboState";
            this.comboState.Size = new System.Drawing.Size(145, 21);
            this.comboState.TabIndex = 78;
            // 
            // ViewEventDetails
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(863, 294);
            this.Controls.Add(this.comboState);
            this.Controls.Add(this.btnUpdate);
            this.Controls.Add(this.btnDelete);
            this.Controls.Add(this.btnGuardar);
            this.Controls.Add(this.btnOpenClub);
            this.Controls.Add(this.dateTimeEndDate);
            this.Controls.Add(this.dateTimeRouteStartDate);
            this.Controls.Add(this.dateTimeStartDate);
            this.Controls.Add(this.txtAddress);
            this.Controls.Add(this.lblState);
            this.Controls.Add(this.txtDescription);
            this.Controls.Add(this.lblDescription);
            this.Controls.Add(this.txtClub);
            this.Controls.Add(this.txtName);
            this.Controls.Add(this.lblEndDate);
            this.Controls.Add(this.lblAddress);
            this.Controls.Add(this.lblRouteStartDate);
            this.Controls.Add(this.lblClub);
            this.Controls.Add(this.lblStartDate);
            this.Controls.Add(this.lblName);
            this.Controls.Add(this.lblTitol);
            this.Name = "ViewEventDetails";
            this.Text = "ViewEventDetails";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion
        public System.Windows.Forms.TextBox txtDescription;
        public System.Windows.Forms.Label lblDescription;
        public System.Windows.Forms.TextBox txtClub;
        public System.Windows.Forms.TextBox txtName;
        public System.Windows.Forms.Label lblEndDate;
        public System.Windows.Forms.Label lblAddress;
        public System.Windows.Forms.Label lblRouteStartDate;
        public System.Windows.Forms.Label lblClub;
        public System.Windows.Forms.Label lblStartDate;
        public System.Windows.Forms.Label lblName;
        public System.Windows.Forms.Label lblTitol;
        public System.Windows.Forms.Label lblState;
        public System.Windows.Forms.TextBox txtAddress;
        public System.Windows.Forms.DateTimePicker dateTimeStartDate;
        public System.Windows.Forms.DateTimePicker dateTimeRouteStartDate;
        public System.Windows.Forms.DateTimePicker dateTimeEndDate;
        public System.Windows.Forms.Button btnOpenClub;
        public System.Windows.Forms.Button btnUpdate;
        public System.Windows.Forms.Button btnDelete;
        public System.Windows.Forms.Button btnGuardar;
        public System.Windows.Forms.ComboBox comboState;
    }
}