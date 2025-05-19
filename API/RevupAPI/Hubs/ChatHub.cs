namespace RevupAPI.Hubs
{
    using Microsoft.AspNetCore.SignalR;
    using System.Collections.Concurrent;

    public class ChatHub : Hub
    {
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
                await Clients.Client(target.Key).SendAsync("ReceiveMessage", Users[Context.ConnectionId], message);
            }
        }

        public async Task JoinGroup(string groupName)
        {
            await Groups.AddToGroupAsync(Context.ConnectionId, groupName);
        }

        public async Task SendMessageToGroup(string groupName, string message)
        {
            await Clients.Group(groupName).SendAsync("ReceiveGroupMessage", Users[Context.ConnectionId], message);
        }

        public override Task OnDisconnectedAsync(Exception? exception)
        {
            Users.TryRemove(Context.ConnectionId, out _);
            return base.OnDisconnectedAsync(exception);
        }
    }

}
