import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EditDataService {
  private editData = signal<any>(null);
  private editMode = signal<boolean>(false);

  setEditData(data: any) {
    this.editData.set(data);
    this.editMode.set(true);
  }

  getEditData() {
    return this.editData();
  }

  isEditMode() {
    return this.editMode();
  }

  clearEditData() {
    this.editData.set(null);
    this.editMode.set(false);
  }
}

