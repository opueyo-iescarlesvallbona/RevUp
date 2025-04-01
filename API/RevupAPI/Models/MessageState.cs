using System;
using System.Collections.Generic;

namespace RevupAPI.Models;

public partial class MessageState
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public virtual ICollection<Message> Messages { get; set; } = new List<Message>();
}
