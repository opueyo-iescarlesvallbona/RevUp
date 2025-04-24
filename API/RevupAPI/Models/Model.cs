using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class Model
{
    public int Id { get; set; }

    public int IdBrand { get; set; }

    public string ModelName { get; set; } = null!;

    public virtual ICollection<Car> Cars { get; set; } = new List<Car>();

    public virtual Brand IdBrandNavigation { get; set; } = null!;
}
