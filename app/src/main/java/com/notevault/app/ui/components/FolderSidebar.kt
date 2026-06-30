package com.notevault.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.notevault.app.data.local.entity.FolderEntity

@Composable
fun FolderSidebar(
    folders: List<FolderEntity>,
    selectedFolderId: Long?,
    onFolderSelected: (Long?) -> Unit,
    onAllNotesSelected: () -> Unit,
    onFavoritesSelected: () -> Unit,
    onTrashSelected: () -> Unit,
    onNewFolder: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp)
        ) {
            // App Title
            Text(
                text = "NoteVault",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // All Notes
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.FolderOpen, contentDescription = null) },
                label = { Text("All Notes") },
                selected = selectedFolderId == null,
                onClick = onAllNotesSelected,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            // Favorites
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary) },
                label = { Text("Favorites") },
                selected = false,
                onClick = onFavoritesSelected,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(modifier = Modifier.height(8.dp))

            // Folders Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FOLDERS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onNewFolder) {
                    Icon(Icons.Filled.Add, contentDescription = "New folder", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New")
                }
            }

            // Folder List
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(folders, key = { it.id }) { folder ->
                    FolderItem(
                        folder = folder,
                        isSelected = folder.id == selectedFolderId,
                        onClick = { onFolderSelected(folder.id) }
                    )
                }
            }

            Divider(modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(modifier = Modifier.height(8.dp))

            // Trash
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                label = { Text("Trash") },
                selected = false,
                onClick = onTrashSelected,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Composable
private fun FolderItem(
    folder: FolderEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else
            Color.Transparent,
        animationSpec = spring(),
        label = "folderBg"
    )

    val folderColor = try {
        Color(folder.colorHex.toColorInt())
    } catch (_: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color dot
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(folderColor)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = folder.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
    }
}
