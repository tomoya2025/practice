package com.example.samuraitravel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;

@Service
public class HouseService {
	private final HouseRepository houseRepository;
	
	public HouseService(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}
	
	//全ての民宿をページング取得
	public Page<House> findAllHouses(Pageable pageable){
		return houseRepository.findAll(pageable);
	}
	
	//キーワード検索
	public Page<House> findHousesByNameLike(String keyword, Pageable pageable){
		return houseRepository.findByNameLike("%" + keyword + "%", pageable);
	}
	
	//IDで民宿を取得
	public Optional<House> findHouseById(Integer id){
		return houseRepository.findById(id);
	}
	
	//民宿のレコード数
	public long countHouses() {
		return houseRepository.count();
	}
	
	//IDが最も大きな民宿を取得する
	public House findFirstHouseByOrderByIdDesc() {
		return houseRepository.findFirstByOrderByIdDesc();
	}
	
	//並び替え
	 public Page<House> findHousesByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable) {
		 return houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + nameKeyword + "%", "%" + addressKeyword + "%", pageable);
	}

     public Page<House> findHousesByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable) {
         return houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + nameKeyword + "%", "%" + addressKeyword + "%", pageable);
    }

     public Page<House> findHousesByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable) {
	     return houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
	}

    public Page<House> findHousesByAddressLikeOrderByPriceAsc(String area, Pageable pageable) {
    	return houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
	}

    public Page<House> findHousesByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable) {
    	return houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
	}

    public Page<House> findHousesByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable) {
    	return houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
	}

    public Page<House> findAllHousesByOrderByCreatedAtDesc(Pageable pageable) {
    	return houseRepository.findAllByOrderByCreatedAtDesc(pageable);
	}

    public Page<House> findAllHousesByOrderByPriceAsc(Pageable pageable) {
    	return houseRepository.findAllByOrderByPriceAsc(pageable);
	}       
    
    public List<House> findTop10HousesByOrderByCreatedAtDesc(){
    	return houseRepository.findTop10ByOrderByCreatedAtDesc();
    }
		
	//民宿を新規作成
	@Transactional
	public void createHouse(HouseRegisterForm houseRegisterForm) {
		House house = new House();
		MultipartFile imageFile = houseRegisterForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}
		
		house.setName(houseRegisterForm.getName());
		house.setDescription(houseRegisterForm.getDescription());
		house.setPrice(houseRegisterForm.getPrice());
		house.setCapacity(houseRegisterForm.getCapacity());
		house.setPostalCode(houseRegisterForm.getPostalCode());
		house.setAddress(houseRegisterForm.getAddress());
		house.setPhoneNumber(houseRegisterForm.getPhoneNumber());
		
		houseRepository.save(house);
	}
	
	//民宿を更新
	@Transactional
	public void updateHouse(HouseEditForm houseEditForm, House house) {
		MultipartFile imageFile = houseEditForm.getImageFile();
		
		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}
		
		house.setName(houseEditForm.getName());
		house.setDescription(houseEditForm.getDescription());
		house.setPrice(houseEditForm.getPrice());
		house.setCapacity(houseEditForm.getCapacity());
		house.setPostalCode(houseEditForm.getPostalCode());
		house.setAddress(houseEditForm.getAddress());
		house.setPhoneNumber(houseEditForm.getPhoneNumber());
		
		houseRepository.save(house);
	}
	
	//民宿を削除
	@Transactional
	public void deleteHouse(House house) {
		houseRepository.delete(house);
	}
	
	//UUID(重複しない一意のID)を使って新しいファイル名を生成
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		
		for(int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] =  UUID.randomUUID().toString();
		}
		
		String hashedFileName = String.join(".", fileNames);
		
		return hashedFileName;
	}
	
	//画像を指定のパスにコピー
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
