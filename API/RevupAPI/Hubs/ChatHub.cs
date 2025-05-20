namespace RevupAPI.Hubs
{
    using Microsoft.AspNetCore.SignalR;
    using Microsoft.EntityFrameworkCore;
    using RevupAPI.Models;
    using System.Collections.Concurrent;

    public class ChatHub : Hub
    {
        private readonly RevupContext _context;

        public ChatHub(RevupContext context)
        {
            _context = context;
        }
        private static ConcurrentDictionary<string, string> Users = new();

        public async Task Register(string memberName)
        {
            Users[Context.ConnectionId] = memberName;
            await Clients.All.SendAsync("UserConnected", memberName);
        }

        public async Task SendMessageToUser(string targetMemberName, string message)
        {
            var target = Users.FirstOrDefault(kvp => kvp.Value == targetMemberName);
            if (target.Key != null)
            {
                var senderMemberName = Users.GetValueOrDefault(Context.ConnectionId);
                var sender = await _context.Members.Where(x => x.Membername.Equals(senderMemberName)).FirstOrDefaultAsync();
                var reciever = await _context.Members.Where(x => x.Membername.Equals(targetMemberName)).FirstOrDefaultAsync();

                var chatMessage = new Message
                {
                    SenderId = sender.Id,
                    ReceiverId = reciever.Id,
                    ContentMessage = message,
                    Datetime = DateTime.UtcNow,
                    StateId = 1
                };
                try
                {
                    _context.Messages.Add(chatMessage);
                    await _context.SaveChangesAsync();
                }
                catch
                {

                }
                
                await Clients.Client(target.Key).SendAsync("ReceiveMessage", Users[Context.ConnectionId], message);

            }
        }

        public async Task JoinGroup(string groupName)
        {
            await Groups.AddToGroupAsync(Context.ConnectionId, groupName);
        }

        public async Task SendMessageToGroup(string groupName, string message)
        {
            var senderMemberName = Users.GetValueOrDefault(Context.ConnectionId);
            var sender = await _context.Members.Where(x => x.Membername.Equals(senderMemberName)).FirstOrDefaultAsync();
            var reciever = await _context.Clubs.Where(x => x.Name.Equals(groupName)).FirstOrDefaultAsync();

            var chatMessage = new Message
            {
                SenderId = sender.Id,
                ReceiverId = reciever.Id,
                ContentMessage = message,
                Datetime = DateTime.UtcNow,
                StateId = 2
            };
            try
            {
                _context.Messages.Add(chatMessage);
                await _context.SaveChangesAsync();
            }
            catch
            {

            }
            await Clients.Group(groupName).SendAsync("ReceiveGroupMessage", Users[Context.ConnectionId], message);
        }

        public override Task OnDisconnectedAsync(Exception? exception)
        {
            Users.TryRemove(Context.ConnectionId, out _);
            return base.OnDisconnectedAsync(exception);
        }
    }

}
