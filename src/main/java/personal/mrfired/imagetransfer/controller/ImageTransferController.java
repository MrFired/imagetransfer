package personal.mrfired.imagetransfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import personal.mrfired.imagetransfer.entity.Image;
import personal.mrfired.imagetransfer.service.ImageService;
import personal.mrfired.imagetransfer.service.storage.exceptions.StorageFileNotFoundException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class ImageTransferController {
    private static final int PAGE_SIZE = 10;
    private static final int MAX_FILENAME_LENGTH = 200;

    private final ImageService imageService;

    @Autowired
    public ImageTransferController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/upload";
    }

    @GetMapping("/content/{name}")
    public ResponseEntity<Resource> getImage(@PathVariable String name) {
        Resource file = imageService.getImageAsResource(name);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + URLEncoder.encode(name, StandardCharsets.UTF_8)
                        .replaceAll("\\+", "%20") + "\"").body(file);
    }

    @GetMapping("/content/preview/{name}")
    public ResponseEntity<Resource> getImagePreview(@PathVariable String name) {
        Resource file = imageService.getImageAsResource(name);

        if (file == null)
            return ResponseEntity.notFound().build();

        MediaType type = MediaTypeFactory.getMediaType(file)
                .orElse(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok().contentType(type).body(file);
    }

    @GetMapping("/delete/{name}")
    public String deleteImage(@PathVariable String name,
                              RedirectAttributes redirectAttributes) {
        imageService.deleteImage(name);

        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message",
                "Изображение " + name + " было успешно удалено.");

        return "redirect:/list";
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        String status = "danger"; // Failure by default

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message",
                    "Не выбрано изображение для загрузки");
        } else if (file.getContentType() == null || !file.getContentType().contains("image")) {
            redirectAttributes.addFlashAttribute("message",
                    "Неправильный формат файла");
        } else if (file.getOriginalFilename() == null) {
            redirectAttributes.addFlashAttribute("message",
                    "Не указано название файла");
        } else if (file.getOriginalFilename().length() > MAX_FILENAME_LENGTH) {
            redirectAttributes.addFlashAttribute("message",
                    "Превышена максимальная длинна названия файла (" + MAX_FILENAME_LENGTH + " символов)");
        } else {
            imageService.uploadImage(SecurityContextHolder.getContext().getAuthentication().getName(), file);

            status = "success";
            redirectAttributes.addFlashAttribute("message",
                    "Вы успешно загрузили " + file.getOriginalFilename() + "!");
        }

        redirectAttributes.addFlashAttribute("status", status);

        return "redirect:/upload";
    }

    @GetMapping("/upload")
    public String upload() {
        return "uploadForm";
    }

    @GetMapping("/list")
    public String getImagesList(Model model,
                                @RequestParam(defaultValue = "1") int page) {
        Page<Image> images = imageService.getImages(page - 1, PAGE_SIZE);

        model.addAttribute("images", images.getContent());
        model.addAttribute("currentPage", images.getNumber() + 1);
        model.addAttribute("totalPages", images.getTotalPages());
        model.addAttribute("pageSize", PAGE_SIZE);

        return "imagesList";
    }

    @ExceptionHandler
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException ignoredEx) {
        return ResponseEntity.notFound().build();
    }
}
