package com.bysoftware.fixedcalendar.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.ui.theme.FixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.theme.PreviewFixedCalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.about),
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoCard(
                title = stringResource(R.string.info_title),
                content = stringResource(R.string.info_desc)
            )
            
            InfoCard(
                title = stringResource(R.string.feaute_title),
                content = stringResource(R.string.feature_desc).trimIndent()
            )
            
            InfoCard(
                title = stringResource(R.string.months_title),
                content = stringResource(R.string.months_desc).trimIndent()
            )
            
            InfoCard(
                title = stringResource(R.string.advantages_title),
                content = stringResource(R.string.advantages_desc).trimIndent()
            )
            
            InfoCard(
                title = stringResource(R.string.history_title),
                content = stringResource(R.string.history_desc)
            )

            PrivacyPolicyRow()
        }
    }
}

@Composable
private fun PrivacyPolicyRow() {
    val context = LocalContext.current
    val url = "https://bysoftware.github.io/fixedcalendar-privacy/"
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                runCatching {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PrivacyTip,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.privacy_policy),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    content: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {
    PreviewFixedCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            InfoScreen(onBackClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardPreview() {
    FixedCalendarTheme {
        InfoCard(
            title = "Örnek Başlık",
            content = "Bu bir örnek içerik metnidir. Bu metin, kartın nasıl görüneceğini göstermek için kullanılmaktadır."
        )
    }
} 