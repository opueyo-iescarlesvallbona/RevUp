using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class Brand
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<Model> Models { get; set; } = new List<Model>();
}
