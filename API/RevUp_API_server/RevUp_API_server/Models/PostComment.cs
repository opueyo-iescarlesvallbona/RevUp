using System;
using System.Collections.Generic;

namespace RevUp_API_server.Models;

public partial class PostComment
{
    public int Id { get; set; }

    public int PostId { get; set; }

    public int MemberId { get; set; }

    public string CommentContent { get; set; } = null!;

    public DateTime Datetime { get; set; }

    public virtual Post Post { get; set; } = null!;
}
