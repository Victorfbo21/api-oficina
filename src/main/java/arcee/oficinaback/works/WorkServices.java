package arcee.oficinaback.works;


import arcee.oficinaback.configs.AppResponse;
import arcee.oficinaback.works.dtos.CreateWorkDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkServices {

    @Autowired
    WorkRepository _workRepository;


    public ResponseEntity<AppResponse> createWork(CreateWorkDto createWorkData, String ownerId){
        var name = createWorkData.getName();
        var category = createWorkData.getCategory();
        var price = createWorkData.getPrice();

        Optional<work_entity> work = _workRepository.findWorkByNameAndOwnerId(name, ownerId);
        System.out.println(work);

        if(work.isPresent()){
            return ResponseEntity.status(400)
                    .body(new AppResponse(
                            null,
                            true,
                            400,
                            "Serviço Já Cadastrado!"));
        }

        work_entity workToCreate = new work_entity();

        workToCreate.setCategory(category);
        workToCreate.setName(name);
        workToCreate.setPrice(price);
        workToCreate.setCode(createWorkData.getCode());
        workToCreate.setCost(createWorkData.getCost());
        workToCreate.setOwnerId(ownerId);
        workToCreate.setDescription(createWorkData.getDescription());

        _workRepository.save(workToCreate);

        return ResponseEntity.status(201)
                .body(new AppResponse(
                        workToCreate.getId(),
                        false,
                        201,
                        "Serviço Criado com Sucesso!"));
    }

    public ResponseEntity<AppResponse> listWorks(String ownerId){

        List<work_entity> works = _workRepository.findAllByOwnerId(ownerId);

        return ResponseEntity
                .status(200)
                .body(new AppResponse(
                        works,
                        false,
                        200,
                        "Serviços Encontrados com Sucesso!"));

    }

    public ResponseEntity<AppResponse> deleteWork(String workId, String ownerId){

        Optional<work_entity> work = _workRepository.findWorkById(workId);

        if(work.isEmpty() || work.get().getDeleted()){
            return ResponseEntity.status(400)
                    .body(new AppResponse(
                            null,
                            true,
                            400,
                            "Serviço Não Encontrado!"));
        }

        var newWork = work.get();

        newWork.setDeleted(true);
        newWork.setActive(false);

        _workRepository.save(newWork);

        return ResponseEntity.status(200)
                .body(new AppResponse(
                        null,
                        false,
                        200,
                        "Serviço Deletado com Sucesso!"));

    }


}
