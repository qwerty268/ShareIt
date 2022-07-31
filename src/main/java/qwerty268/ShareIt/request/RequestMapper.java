package qwerty268.ShareIt.request;

public class RequestMapper {
    public static RequestDTO toDTO(Request request) {
        return new RequestDTO(request.getId(), request.getDescription(), request.getRequestorId());
    }
}
