namespace RevupCrud.View
{
    partial class ViewEvents
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
            this.txtClub = new System.Windows.Forms.TextBox();
            this.btnInsert = new System.Windows.Forms.Button();
            this.btnSearch = new System.Windows.Forms.Button();
            this.lblClub = new System.Windows.Forms.Label();
            this.txtName = new System.Windows.Forms.TextBox();
            this.lblName = new System.Windows.Forms.Label();
            this.dataGridView = new System.Windows.Forms.DataGridView();
            this.lblState = new System.Windows.Forms.Label();
            this.comboState = new System.Windows.Forms.ComboBox();
            this.dateTimeEndFrom = new System.Windows.Forms.DateTimePicker();
            this.dateTimeEndTo = new System.Windows.Forms.DateTimePicker();
            this.lblStartDate = new System.Windows.Forms.Label();
            this.lblEndDate = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.dateTimeStartTo = new System.Windows.Forms.DateTimePicker();
            this.dateTimeStartFrom = new System.Windows.Forms.DateTimePicker();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView)).BeginInit();
            this.SuspendLayout();
            // 
            // txtClub
            // 
            this.txtClub.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtClub.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.Suggest;
            this.txtClub.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.CustomSource;
            this.txtClub.Location = new System.Drawing.Point(90, 49);
            this.txtClub.Name = "txtClub";
            this.txtClub.Size = new System.Drawing.Size(311, 20);
            this.txtClub.TabIndex = 29;
            // 
            // btnInsert
            // 
            this.btnInsert.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnInsert.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnInsert.Location = new System.Drawing.Point(880, 22);
            this.btnInsert.Name = "btnInsert";
            this.btnInsert.Size = new System.Drawing.Size(75, 55);
            this.btnInsert.TabIndex = 28;
            this.btnInsert.Text = "Insert";
            this.btnInsert.UseVisualStyleBackColor = true;
            // 
            // btnSearch
            // 
            this.btnSearch.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnSearch.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnSearch.Location = new System.Drawing.Point(783, 21);
            this.btnSearch.Name = "btnSearch";
            this.btnSearch.Size = new System.Drawing.Size(75, 55);
            this.btnSearch.TabIndex = 27;
            this.btnSearch.Text = "Search";
            this.btnSearch.UseVisualStyleBackColor = true;
            // 
            // lblClub
            // 
            this.lblClub.AutoSize = true;
            this.lblClub.Location = new System.Drawing.Point(16, 51);
            this.lblClub.Name = "lblClub";
            this.lblClub.Size = new System.Drawing.Size(31, 13);
            this.lblClub.TabIndex = 26;
            this.lblClub.Text = "Club:";
            // 
            // txtName
            // 
            this.txtName.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.txtName.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.Suggest;
            this.txtName.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.CustomSource;
            this.txtName.Location = new System.Drawing.Point(90, 20);
            this.txtName.Name = "txtName";
            this.txtName.Size = new System.Drawing.Size(311, 20);
            this.txtName.TabIndex = 25;
            // 
            // lblName
            // 
            this.lblName.AutoSize = true;
            this.lblName.Location = new System.Drawing.Point(15, 22);
            this.lblName.Name = "lblName";
            this.lblName.Size = new System.Drawing.Size(69, 13);
            this.lblName.TabIndex = 24;
            this.lblName.Text = "Event Name:";
            // 
            // dataGridView
            // 
            this.dataGridView.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.dataGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.AllCells;
            this.dataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView.Location = new System.Drawing.Point(22, 125);
            this.dataGridView.MultiSelect = false;
            this.dataGridView.Name = "dataGridView";
            this.dataGridView.ReadOnly = true;
            this.dataGridView.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dataGridView.Size = new System.Drawing.Size(933, 413);
            this.dataGridView.TabIndex = 23;
            // 
            // lblState
            // 
            this.lblState.AutoSize = true;
            this.lblState.Location = new System.Drawing.Point(16, 83);
            this.lblState.Name = "lblState";
            this.lblState.Size = new System.Drawing.Size(35, 13);
            this.lblState.TabIndex = 35;
            this.lblState.Text = "State:";
            // 
            // comboState
            // 
            this.comboState.FormattingEnabled = true;
            this.comboState.Location = new System.Drawing.Point(90, 80);
            this.comboState.Name = "comboState";
            this.comboState.Size = new System.Drawing.Size(311, 21);
            this.comboState.TabIndex = 36;
            // 
            // dateTimeEndFrom
            // 
            this.dateTimeEndFrom.CustomFormat = "dd/MM/yyyy";
            this.dateTimeEndFrom.Format = System.Windows.Forms.DateTimePickerFormat.Custom;
            this.dateTimeEndFrom.Location = new System.Drawing.Point(650, 46);
            this.dateTimeEndFrom.Name = "dateTimeEndFrom";
            this.dateTimeEndFrom.ShowCheckBox = true;
            this.dateTimeEndFrom.Size = new System.Drawing.Size(112, 20);
            this.dateTimeEndFrom.TabIndex = 30;
            // 
            // dateTimeEndTo
            // 
            this.dateTimeEndTo.CustomFormat = "dd/MM/yyyy";
            this.dateTimeEndTo.Format = System.Windows.Forms.DateTimePickerFormat.Custom;
            this.dateTimeEndTo.Location = new System.Drawing.Point(650, 78);
            this.dateTimeEndTo.Name = "dateTimeEndTo";
            this.dateTimeEndTo.ShowCheckBox = true;
            this.dateTimeEndTo.Size = new System.Drawing.Size(112, 20);
            this.dateTimeEndTo.TabIndex = 31;
            // 
            // lblStartDate
            // 
            this.lblStartDate.AutoSize = true;
            this.lblStartDate.Location = new System.Drawing.Point(608, 48);
            this.lblStartDate.Name = "lblStartDate";
            this.lblStartDate.Size = new System.Drawing.Size(33, 13);
            this.lblStartDate.TabIndex = 33;
            this.lblStartDate.Text = "From:";
            // 
            // lblEndDate
            // 
            this.lblEndDate.AutoSize = true;
            this.lblEndDate.Location = new System.Drawing.Point(611, 81);
            this.lblEndDate.Name = "lblEndDate";
            this.lblEndDate.Size = new System.Drawing.Size(23, 13);
            this.lblEndDate.TabIndex = 34;
            this.lblEndDate.Text = "To:";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(420, 82);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(23, 13);
            this.label1.TabIndex = 40;
            this.label1.Text = "To:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(419, 49);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(33, 13);
            this.label2.TabIndex = 39;
            this.label2.Text = "From:";
            // 
            // dateTimeStartTo
            // 
            this.dateTimeStartTo.CustomFormat = "dd/MM/yyyy";
            this.dateTimeStartTo.Format = System.Windows.Forms.DateTimePickerFormat.Custom;
            this.dateTimeStartTo.Location = new System.Drawing.Point(460, 79);
            this.dateTimeStartTo.Name = "dateTimeStartTo";
            this.dateTimeStartTo.ShowCheckBox = true;
            this.dateTimeStartTo.Size = new System.Drawing.Size(117, 20);
            this.dateTimeStartTo.TabIndex = 38;
            // 
            // dateTimeStartFrom
            // 
            this.dateTimeStartFrom.CustomFormat = "dd/MM/yyyy";
            this.dateTimeStartFrom.Format = System.Windows.Forms.DateTimePickerFormat.Custom;
            this.dateTimeStartFrom.Location = new System.Drawing.Point(460, 47);
            this.dateTimeStartFrom.Name = "dateTimeStartFrom";
            this.dateTimeStartFrom.ShowCheckBox = true;
            this.dateTimeStartFrom.Size = new System.Drawing.Size(117, 20);
            this.dateTimeStartFrom.TabIndex = 37;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(486, 20);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(55, 13);
            this.label3.TabIndex = 41;
            this.label3.Text = "Start Date";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(674, 20);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(52, 13);
            this.label4.TabIndex = 42;
            this.label4.Text = "End Date";
            // 
            // ViewEvents
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(971, 558);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.dateTimeStartTo);
            this.Controls.Add(this.dateTimeStartFrom);
            this.Controls.Add(this.comboState);
            this.Controls.Add(this.lblState);
            this.Controls.Add(this.lblEndDate);
            this.Controls.Add(this.lblStartDate);
            this.Controls.Add(this.dateTimeEndTo);
            this.Controls.Add(this.dateTimeEndFrom);
            this.Controls.Add(this.txtClub);
            this.Controls.Add(this.btnInsert);
            this.Controls.Add(this.btnSearch);
            this.Controls.Add(this.lblClub);
            this.Controls.Add(this.txtName);
            this.Controls.Add(this.lblName);
            this.Controls.Add(this.dataGridView);
            this.Name = "ViewEvents";
            this.Text = "ViewEvents";
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        public System.Windows.Forms.TextBox txtClub;
        public System.Windows.Forms.Button btnInsert;
        public System.Windows.Forms.Button btnSearch;
        public System.Windows.Forms.Label lblClub;
        public System.Windows.Forms.TextBox txtName;
        public System.Windows.Forms.Label lblName;
        public System.Windows.Forms.DataGridView dataGridView;
        public System.Windows.Forms.Label lblState;
        public System.Windows.Forms.ComboBox comboState;
        public System.Windows.Forms.DateTimePicker dateTimeEndFrom;
        public System.Windows.Forms.DateTimePicker dateTimeEndTo;
        public System.Windows.Forms.Label lblStartDate;
        public System.Windows.Forms.Label lblEndDate;
        public System.Windows.Forms.Label label1;
        public System.Windows.Forms.Label label2;
        public System.Windows.Forms.DateTimePicker dateTimeStartTo;
        public System.Windows.Forms.DateTimePicker dateTimeStartFrom;
        public System.Windows.Forms.Label label3;
        public System.Windows.Forms.Label label4;
    }
}