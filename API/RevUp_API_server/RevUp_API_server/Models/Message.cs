using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class Message
{
    public int SenderId { get; set; }

    public int ReceiverId { get; set; }

    public DateTime Datetime { get; set; }

    public string ContentMessage { get; set; } = null!;

    public int StateId { get; set; }

    public virtual Member Receiver { get; set; } = null!;

    public virtual Member Sender { get; set; } = null!;

    public virtual MessageState State { get; set; } = null!;
}
