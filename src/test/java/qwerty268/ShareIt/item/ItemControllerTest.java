package qwerty268.ShareIt.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import qwerty268.ShareIt.comment.CommentDTO;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    ItemServiceImpl itemService;
    @InjectMocks
    ItemController itemController;

    MockMvc mvc;
    final ObjectMapper mapper = new ObjectMapper();

    ItemDTO item = new ItemDTO(1L, "Дрель", "Очень крутая дрель", true, null, 1L);

    ItemWithBookingsAndCommentsDTO itemDTO = new ItemWithBookingsAndCommentsDTO(1L, null, null, null, null, null,
            null, null, null);

    CommentDTO commentDTO = new CommentDTO(1L, "comment", null, null, null, null);


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();
    }

    @Test
    void saveItem() throws Exception {
        Mockito.when(itemService.save(item, 1L))
                .thenReturn(item);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void updateItem() throws Exception {
        Mockito.when(itemService.update(item, 1L, 1L))
                .thenReturn(item);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void findItems() throws Exception {
        List<ItemWithBookingsAndCommentsDTO> dtoList = List.of(itemDTO);

        Mockito.when(itemService.findAll(anyLong(), anyInt(), anyInt()))
                .thenReturn(dtoList);

        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(itemDTO))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class));
    }

    @Test
    void getItem() throws Exception {
        Mockito.when(itemService.findById(anyLong(), anyLong()))
                .thenReturn(itemDTO);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testFindItems() throws Exception {
        List<ItemDTO> itemDTOS = List.of(item);

        Mockito.when(itemService.findItemsByParam(anyString(), anyInt(), anyInt()))
                .thenReturn(itemDTOS);

        mvc.perform(get("/items/search?text=x")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class));
    }

    @Test
    void addComment() throws Exception {
        Mockito.when(itemService.addComment(any(), anyLong(), anyLong()))
                .thenReturn(commentDTO);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDTO))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }
}