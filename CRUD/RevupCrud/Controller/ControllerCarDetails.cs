using RepositoriRevUp.Model;
using RevupCrud.Model;
using RevupCrud.View;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RevupCrud.Controller
{
    public class ControllerCarDetails
    {
        RepositoriCrud r = new RepositoriCrud();
        ViewCarDetails f;
        car car;

        void SetListeners()
        {
            f.btnDelete.Click += BtnDelete_Click;
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            r.DeleteCar(car);
            f.Close();
        }

        void LoadData()
        {
            if (car != null)
            {
                f.lblTitol.Text = "Car - " + car.id.ToString();
                f.txtBrand.Text = car.model.brand.name;
                f.txtModel.Text = car.model.model_name;
                f.txtYear.Text = car.model_year.ToString();
                f.txtHp.Text = car.horse_power.ToString();
            }
        }

        public ControllerCarDetails(car car, ViewCarDetails form)
        {
            if (car != null)
            {
                this.car = car;
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
