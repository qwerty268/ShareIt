package qwerty268.ShareIt.request;

public class RequestMapper {
    public static RequestDTO toDTO(Request request) {
        return new RequestDTO(request.getId(), request.getDescription(), request.getRequestorId(), request.getCreated());
    }

    public static Request fromDTO(RequestDTO requestDTO) {
        return new Request(requestDTO.getId(), requestDTO.getDescription(), requestDTO.getRequestorId());
    }
}
