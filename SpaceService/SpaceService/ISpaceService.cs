using SpaceService.Model;
using SpaceService.Model.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace SpaceService
{
    [ServiceContract]
    public interface ISpaceService
    {
        [OperationContract]
        [WebInvoke(Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        void SetName(string deviceId, string name);

        [OperationContract]
        [WebInvoke(Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        void UploadHighScore(string deviceId, int score);

        [OperationContract]
        [WebGet(ResponseFormat = WebMessageFormat.Json)]
        List<HighScoreDTO> HighScores();

        [OperationContract]
        [WebInvoke(Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        StartMultiplayerResponse StartMultiplayer(string deviceId);

        [OperationContract]
        [WebInvoke(Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        void QuitLobby(string deviceId);

        [OperationContract]
        [WebInvoke(Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        TickResponse Tick(string deviceId, Vector position, Vector velocity);

        [OperationContract]
        [WebInvoke(Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        long Delay(long timestamp);

        [OperationContract]
        [WebGet(ResponseFormat = WebMessageFormat.Json)]
        long ServerTime();

        [OperationContract]
        [WebInvoke(Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        void Finish(string deviceId, int score);

        [OperationContract]
        [WebInvoke(Method = "POST", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        string Result(string deviceId);

        // Development
        [OperationContract]
        [WebGet(ResponseFormat = WebMessageFormat.Json)]
        List<string> Lobby();

        [OperationContract]
        [WebGet(ResponseFormat = WebMessageFormat.Json)]
        List<Match> Matches();

        [OperationContract]
        [WebGet(ResponseFormat = WebMessageFormat.Json)]
        void Reset();


    }
}
